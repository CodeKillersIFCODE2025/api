package br.com.codekillers.zelo.Service;

import br.com.codekillers.zelo.DTO.Mapper.TaskMapper;
import br.com.codekillers.zelo.DTO.Request.TaskRequest;
import br.com.codekillers.zelo.DTO.Response.TaskResponse;
import br.com.codekillers.zelo.Domain.Elderly;
import br.com.codekillers.zelo.Domain.Responsible;
import br.com.codekillers.zelo.Domain.Task;
import br.com.codekillers.zelo.Utils.Date;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class TaskService {

    private static final String COLLECTION_NAME = "Task";
    private final Firestore firestore;
    CollectionReference tasksCollection;


    @Autowired
    public TaskService(Firestore firestore) {
        this.firestore = firestore;
        this.tasksCollection = firestore.collection(COLLECTION_NAME);
    }

    @Autowired
    private ResponsibleService responsibleService;

    @Autowired
    private ElderlyService elderlyService;

    public void addTaskForElderly(TaskRequest taskRequest, UserDetails userDetails) {
        try {
            Responsible responsible = responsibleService
                    .getResponsibleByEmail(userDetails.getUsername()).get();

            Elderly elderly = responsible.getElderly();

            Task task = TaskMapper.toEntity(taskRequest);
            task.setUserId(elderly.getId());

            ApiFuture<DocumentReference> documentReferenceApiFuture = tasksCollection.add(task);

            DocumentReference documentReference = documentReferenceApiFuture.get();

            String documentId = documentReference.getId();
            task.setId(documentId);
            documentReference.set(task);

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public List<TaskResponse> listTasksForTheDay(String email) {
        Elderly elderly = elderlyService.getElderlyByEmail(email).get();

        try {
            ApiFuture<QuerySnapshot> query = tasksCollection.whereEqualTo("userId", elderly.getId())
                    .orderBy("nextActionDue")
                    .get();
            QuerySnapshot querySnapshot = query.get();

            List<Task> foundTasks = new ArrayList<>();
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                foundTasks.add(document.toObject(Task.class));
            }

            return foundTasks.stream().filter(task ->
                            Date.isToday(task.getNextActionDue()))
                    .map(TaskMapper::toResponse)
                    .toList();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }



    public boolean completeTaskForTheDay(String taskID) {
        try {
            ApiFuture<QuerySnapshot> query = tasksCollection.whereEqualTo("id", taskID).get();
            QuerySnapshot querySnapshot = query.get();

            List<Task> foundTasks = new ArrayList<>();
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                foundTasks.add(document.toObject(Task.class));
            }

            Task task = foundTasks.getFirst();

            if (!Date.isToday(task.getNextActionDue())) {
                return false; // verificação para só poder completar tarefas do dia de hoje
            }

            DocumentReference taskReferenceDoc = tasksCollection.document(task.getId());

            if (!task.isRepeated()) {
                taskReferenceDoc.delete();
                return true; //se não for repetida, ao invés de alterar a data para a próxima, deleta a tarefa
            }

            task.setNextActionDue(Date.calculateNextDate(task.getNextActionDue(), task.getFrequencyUnit()));
            taskReferenceDoc.set(task);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Task> listTasksByElderly(String elderlyId) {
        try {
            ApiFuture<QuerySnapshot> query = tasksCollection.whereEqualTo("userId", elderlyId)
                    .orderBy("nextActionDue")
                    .get();
            QuerySnapshot querySnapshot = query.get();

            List<Task> foundTasks = new ArrayList<>();
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                foundTasks.add(document.toObject(Task.class));
            }

            return foundTasks;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public HashMap<DayOfWeek, List<TaskResponse>> listTasksForTheWeek(String email) {
        Responsible responsible = responsibleService.getResponsibleByEmail(email).get();

        if (responsible.getElderly() == null){
            return toWeekList(new ArrayList<>());
        }

        return toWeekList(
                listTasksByElderly(responsible.getElderly().getId())
        );
    }

    private HashMap<DayOfWeek, List<TaskResponse>> toWeekList(List<Task> tasks) {
        List<Task> weekTasks = tasks.stream().filter(task ->{
            LocalDate taskDate = Date.toLocalDateTime(task.getNextActionDue()).toLocalDate();
            LocalDate yesterday = LocalDate.now().minusDays(1);
            LocalDate sevenDaysLater = LocalDate.now().plusDays(7);


            return taskDate.isBefore(sevenDaysLater) && taskDate.isAfter(yesterday);
        }).toList();

        HashMap<DayOfWeek, List<TaskResponse>> result = new HashMap<>();
        result.put(DayOfWeek.MONDAY, new ArrayList<>());
        result.put(DayOfWeek.TUESDAY, new ArrayList<>());
        result.put(DayOfWeek.WEDNESDAY, new ArrayList<>());
        result.put(DayOfWeek.THURSDAY, new ArrayList<>());
        result.put(DayOfWeek.FRIDAY, new ArrayList<>());
        result.put(DayOfWeek.SATURDAY, new ArrayList<>());
        result.put(DayOfWeek.SUNDAY, new ArrayList<>());

        weekTasks.forEach(task -> {
            switch (
                    Date.toLocalDateTime(task.getNextActionDue()).getDayOfWeek()
            ) {
                case MONDAY -> result.get(DayOfWeek.MONDAY).add(TaskMapper.toResponse(task));
                case TUESDAY -> result.get(DayOfWeek.TUESDAY).add(TaskMapper.toResponse(task));
                case WEDNESDAY -> result.get(DayOfWeek.WEDNESDAY).add(TaskMapper.toResponse(task));
                case THURSDAY -> result.get(DayOfWeek.THURSDAY).add(TaskMapper.toResponse(task));
                case FRIDAY -> result.get(DayOfWeek.FRIDAY).add(TaskMapper.toResponse(task));
                case SATURDAY -> result.get(DayOfWeek.SATURDAY).add(TaskMapper.toResponse(task));
                case SUNDAY -> result.get(DayOfWeek.SUNDAY).add(TaskMapper.toResponse(task));
            }
        });

        return result;
    }
}
