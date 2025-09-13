package br.com.codekillers.zelo.Service;

import br.com.codekillers.zelo.DTO.Mapper.ResponsibleMapper;
import br.com.codekillers.zelo.DTO.Mapper.TaskMapper;
import br.com.codekillers.zelo.DTO.Request.ResponsibleRequest;

import br.com.codekillers.zelo.DTO.Request.TaskRequest;

import br.com.codekillers.zelo.DTO.Response.ResponsibleResponse;
import br.com.codekillers.zelo.Domain.Elderly;
import br.com.codekillers.zelo.Domain.Responsible;
import br.com.codekillers.zelo.Domain.Task;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static br.com.codekillers.zelo.Utils.Cryptography.encryptPassword;

@Service
public class ResponsibleService {

    private final CollectionReference responsibles;

    @Autowired
    public ResponsibleService(Firestore firestore) {
        this.responsibles = firestore.collection("Responsible");
    }

    public String createResponsible(ResponsibleRequest request) {
        Optional<Responsible> existingResponsible = getResponsibleByEmail(request.getEmail());

        if (existingResponsible.isPresent()) return "Email already in use";

        Responsible responsible = ResponsibleMapper.toEntity(request);

        try {
            ApiFuture<DocumentReference> documentReferenceApiFuture = responsibles.add(responsible);

            DocumentReference documentReference = documentReferenceApiFuture.get();
            String documentId = documentReference.getId();

            responsible.setId(documentId);
            documentReference.set(responsible);

            return null;
        } catch (InterruptedException | ExecutionException e) {
            return  e.getMessage();
        }
    }

    public Optional<Responsible> getResponsibleByEmail(String email) {
        try {
            ApiFuture<QuerySnapshot> query = responsibles.whereEqualTo("email", email).get();

            QuerySnapshot querySnapshot = query.get();

            List<Responsible> foundResponsibles = new ArrayList<>();
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                foundResponsibles.add(document.toObject(Responsible.class));
            }

            return foundResponsibles.stream().findFirst();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void addElderly(Responsible responsible, Elderly elderly) {
        DocumentReference responsibleDocRef = responsibles
                .document(responsible.getId());

        Map<String, Object> updates = new HashMap<>();
        updates.put("elderly", elderly);

        responsibleDocRef.update(updates);

    }

    public ResponsibleResponse getResponsibleByElderly(String elderlyEmail) {

        try {
            ApiFuture<QuerySnapshot> query = responsibles.get();

            QuerySnapshot querySnapshot = query.get();

            List<Responsible> foundResponsibles = new ArrayList<>();
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                foundResponsibles.add(document.toObject(Responsible.class));
            }

            Responsible responsible = foundResponsibles.stream()
                    .filter(r -> r.getElderly().getEmail().equals(elderlyEmail))
                    .findFirst().orElse(null);

            return responsible != null
                    ? ResponsibleMapper.toResponse(responsible)
                    : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}