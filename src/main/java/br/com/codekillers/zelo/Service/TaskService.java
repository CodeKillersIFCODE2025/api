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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
                    .getResponsibleByEmail(userDetails.getUsername())
                    .get();

            Elderly elderly = responsible.getElderly();

            Task task = TaskMapper.toEntity(taskRequest);
            task.setUserId(elderly.getId());

            ApiFuture<DocumentReference> documentReferenceApiFuture = tasksCollection.add(task);

            DocumentReference documentReference = documentReferenceApiFuture.get();

            String documentId = documentReference.getId();
            task.setId(documentId);
            documentReference.set(task);

            elderly.addTask(task);
            elderlyService.updateElderly(elderly);
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

            task.setNextActionDue(Date.calculateNextDate(task.getNextActionDue(), task.getFrequencyUnit()));

            DocumentReference taskReferenceDoc = tasksCollection.document(task.getId());
            taskReferenceDoc.set(task);

            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
