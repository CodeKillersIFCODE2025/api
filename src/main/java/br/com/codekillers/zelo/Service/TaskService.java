package br.com.codekillers.zelo.Service;

import br.com.codekillers.zelo.DTO.Mapper.TaskMapper;
import br.com.codekillers.zelo.DTO.Request.TaskRequest;
import br.com.codekillers.zelo.Domain.Elderly;
import br.com.codekillers.zelo.Domain.Responsible;
import br.com.codekillers.zelo.Domain.Task;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class TaskService {

    private static final String COLLECTION_NAME = "Task";
    private final Firestore firestore;

    @Autowired
    public TaskService(Firestore firestore) {
        this.firestore = firestore;
    }

    @Autowired
    private ResponsibleService responsibleService;

    @Autowired
    private ElderlyService elderlyService;

    public void addTaskForElderly(TaskRequest taskRequest, UserDetails userDetails) throws ExecutionException, InterruptedException {
        Responsible responsible = responsibleService
                .getResponsibleByEmail(userDetails.getUsername())
                .get();

        Elderly elderly = responsible.getElderly();

        Task task = TaskMapper.toEntity(taskRequest);
        task.setUserId(elderly.getId());

        CollectionReference tasks = firestore.collection(COLLECTION_NAME);

        ApiFuture<DocumentReference> documentReferenceApiFuture = tasks.add(task);

        DocumentReference documentReference = documentReferenceApiFuture.get();

        String documentId = documentReference.getId();
        task.setId(documentId);
        documentReference.set(task);

        elderly.addTask(task);
        elderlyService.updateElderly(elderly);
    }

}
