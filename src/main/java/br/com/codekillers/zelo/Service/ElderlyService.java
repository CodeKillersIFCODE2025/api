package br.com.codekillers.zelo.Service;

import br.com.codekillers.zelo.DTO.Mapper.ElderlyMapper;
import br.com.codekillers.zelo.DTO.Mapper.ResponsibleMapper;
import br.com.codekillers.zelo.DTO.Request.ElderlyRequest;
import br.com.codekillers.zelo.DTO.Request.ResponsibleRequest;
import br.com.codekillers.zelo.Domain.Elderly;
import br.com.codekillers.zelo.Domain.Responsible;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.util.*;

import java.util.concurrent.ExecutionException;

import static br.com.codekillers.zelo.Utils.Cryptography.encryptPassword;

@Service
public class ElderlyService {

    private static final String COLLECTION_NAME = "Elderly";
    private final Firestore firestore;

    @Autowired
    public ElderlyService(Firestore firestore) {
        this.firestore = firestore;
    }

    @Autowired
    private ResponsibleService responsibleService;


    public String createElderly(ElderlyRequest request, UserDetails userDetails) {
        try {

            Responsible responsible = responsibleService.getResponsibleByEmail(userDetails.getUsername()).get();

            Elderly elderly = ElderlyMapper.toEntity(request);
            elderly.setPassword(encryptPassword(request.getPassword()));

            CollectionReference elderies = firestore.collection(COLLECTION_NAME);
            ApiFuture<DocumentReference> documentReferenceApiFuture = elderies.add(elderly);
            DocumentReference documentReference = documentReferenceApiFuture.get();

            String documentId = documentReference.getId();
            elderly.setId(documentId);
            documentReference.set(elderly);

            responsibleService.addElderly(responsible, elderly);

            return documentId;

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateElderly(Elderly elderly) {
        DocumentReference elderlyReferenceDoc = firestore.collection(COLLECTION_NAME)
                .document(elderly.getId());

        elderlyReferenceDoc.set(elderly);
    }

    public Optional<Elderly> getElderlyByEmail(String email) {
        try {
            CollectionReference elderliesCollection = firestore.collection(COLLECTION_NAME);

            ApiFuture<QuerySnapshot> query = elderliesCollection.whereEqualTo("email", email).get();

            QuerySnapshot querySnapshot = query.get();

            List<Elderly> foundElderlies = new ArrayList<>();
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                foundElderlies.add(document.toObject(Elderly.class));
            }

            return foundElderlies.stream().findFirst();

        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
