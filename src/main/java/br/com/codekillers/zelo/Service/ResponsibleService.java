package br.com.codekillers.zelo.Service;

import br.com.codekillers.zelo.DTO.Mapper.ResponsibleMapper;
import br.com.codekillers.zelo.DTO.Mapper.TaskMapper;
import br.com.codekillers.zelo.DTO.Request.ResponsibleRequest;

import br.com.codekillers.zelo.DTO.Request.TaskRequest;

import br.com.codekillers.zelo.Domain.Elderly;
import br.com.codekillers.zelo.Domain.Responsible;
import br.com.codekillers.zelo.Domain.Task;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static br.com.codekillers.zelo.Utils.Cryptography.encryptPassword;

@org.springframework.stereotype.Service
public class ResponsibleService {

    private static final String COLLECTION_NAME = "Responsible";
    private final Firestore firestore;

    @Autowired
    public ResponsibleService(Firestore firestore) {
        this.firestore = firestore;
    }


    public String createResponsible(ResponsibleRequest request) throws InterruptedException, ExecutionException {
        Responsible responsible = ResponsibleMapper.toEntity(request);
        responsible.setPassword(
            encryptPassword(request.getPassword())
        );

        CollectionReference responsibles = firestore.collection(COLLECTION_NAME);

        ApiFuture<DocumentReference> documentReferenceApiFuture = responsibles.add(responsible);

        DocumentReference documentReference = documentReferenceApiFuture.get();

        String documentId = documentReference.getId();

        responsible.setId(documentId);
        documentReference.set(responsible);

        return documentId;
    }

    public Optional<Responsible> getResponsibleByEmail(String email) {
        try {
            CollectionReference responsiblesCollection = firestore.collection(COLLECTION_NAME);

            ApiFuture<QuerySnapshot> query = responsiblesCollection.whereEqualTo("email", email).get();

            QuerySnapshot querySnapshot = query.get();

            List<Responsible> foundResponsibles = new ArrayList<>();
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                foundResponsibles.add(document.toObject(Responsible.class));
            }

            return foundResponsibles.stream().findFirst();
        } catch (Exception e){
            return Optional.empty();
        }
    }


    public void addElderly(Responsible responsible, Elderly elderly) {
        DocumentReference responsibleDocRef = firestore.collection(COLLECTION_NAME)
                .document(responsible.getId());

        Map<String, Object> updates = new HashMap<>();
        updates.put("elderly", elderly);

        responsibleDocRef.update(updates);

    }
  
}