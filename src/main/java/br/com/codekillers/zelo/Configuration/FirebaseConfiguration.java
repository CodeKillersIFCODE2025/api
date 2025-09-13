package br.com.codekillers.zelo.Configuration;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Firestore;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfiguration {

    //Initializing firebase
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        //Load service key from file
        InputStream serviceAccount = new ClassPathResource("zelo-f3f7e-firebase-adminsdk-fbsvc-fc3fdf2316.json")
                .getInputStream();

        // Build Firebase options using the service account credentials
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        // Initialize Firebase app
        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        } else {
            return FirebaseApp.getInstance(); // Return the existing default app
        }
    }

    // This method provides the Firestore client, using the initialized FirebaseApp
    @Bean
    public Firestore firestore() throws IOException {
        FirebaseApp firebaseApp = firebaseApp();

        return FirestoreClient.getFirestore(firebaseApp);
    }
}
