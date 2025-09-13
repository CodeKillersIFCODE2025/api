package br.com.codekillers.zelo.Service;

import br.com.codekillers.zelo.DTO.Mapper.ResponsibleMapper;
import br.com.codekillers.zelo.DTO.Request.ResponsibleRequest;
import br.com.codekillers.zelo.Domain.Responsible;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.concurrent.ExecutionException;

@org.springframework.stereotype.Service
public class ResponsibleService {

    private static final String COLLECTION_NAME = "Responsible"; // Your Firestore collection name

    private final Firestore firestore; // Inject the Firestore client

    @Autowired
    public ResponsibleService(Firestore firestore) {
        this.firestore = firestore;
    }

    /**
     * Creates a new Responsible and saves it on the database.
     *
     * @param request The Responsible object to save.
     * @return The ID of the newly created document.
     * @throws InterruptedException if the thread is interrupted while waiting.
     * @throws if the computation threw an exception.
     */
    public String createResponsible(ResponsibleRequest request) throws InterruptedException, ExecutionException {
        Responsible responsible = ResponsibleMapper.toEntity(request);

        CollectionReference responsibles = firestore.collection(COLLECTION_NAME);

        // Add the Responsible object to the collection.
        // Firestore automatically maps the POJO fields to document fields.
        // It also generates a unique document ID for this new document.
        ApiFuture<DocumentReference> documentReferenceApiFuture = responsibles.add(responsible);

        // .get() blocks until the operation is complete and returns the DocumentReference
        DocumentReference documentReference = documentReferenceApiFuture.get();

        // You can get the auto-generated ID from the DocumentReference
        String documentId = documentReference.getId();

        // Optionally, you might want to set the ID back into your Responsible object
        // responsible.setId(documentId);
        responsible.setId(documentId);

        System.out.println("Added Responsible with ID: " + documentId);
        return documentId;
    }
}
