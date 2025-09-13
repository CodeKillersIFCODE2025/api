package br.com.codekillers.zelo.Service;

import br.com.codekillers.zelo.DTO.Mapper.ElderlyMapper;
import br.com.codekillers.zelo.DTO.Request.ElderlyRequest;
import br.com.codekillers.zelo.DTO.Response.ElderlyResponse;
import br.com.codekillers.zelo.Domain.Elderly;
import br.com.codekillers.zelo.Domain.Responsible;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class ElderlyService {

    private final CollectionReference elderies;

    @Autowired
    public ElderlyService(Firestore firestore) {
        this.elderies = firestore.collection("Elderly");
    }

    @Autowired
    private ResponsibleService responsibleService;

    public String createElderly(ElderlyRequest request, UserDetails userDetails) {
        Optional<Elderly> existingElderly = getElderlyByEmail(request.getEmail());

        if (existingElderly.isPresent()) return  "Email already in use";

        try {
            Responsible responsible = responsibleService.getResponsibleByEmail(userDetails.getUsername()).get();

            Elderly elderly = ElderlyMapper.toEntity(request);

            ApiFuture<DocumentReference> documentReferenceApiFuture = elderies.add(elderly);

            DocumentReference documentReference = documentReferenceApiFuture.get();
            String documentId = documentReference.getId();

            elderly.setId(documentId);
            documentReference.set(elderly);

            responsibleService.addElderly(responsible, elderly);
            return null;

        } catch (InterruptedException | ExecutionException e) {
            return e.getMessage();
        }
    }

    public void updateElderly(Elderly elderly) {
        DocumentReference elderlyReferenceDoc = elderies
                .document(elderly.getId());

        elderlyReferenceDoc.set(elderly);
    }

    public Optional<Elderly> getElderlyByEmail(String email) {
        try {
            ApiFuture<QuerySnapshot> query = elderies.whereEqualTo("email", email).get();

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

    public ElderlyResponse dailyCheckIn(String email) {
        Elderly elderly = getElderlyByEmail(email).get();

        elderly.setLastCheckIn(Timestamp.now());
        updateElderly(elderly);

        return ElderlyMapper.toResponse(elderly);
    }
}
