package com.gentlemonster.Configurations.FirebaseCloud;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

@Configuration
public class FirebaseConfig {
    @Bean
    public FirebaseMessaging firebaseMessaging() throws Exception {
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource("firebase-service-account.json").getInputStream());
        FirebaseOptions firebaseOptions = FirebaseOptions
                .builder()
                .setCredentials(googleCredentials)
                .build();
        FirebaseApp notificationApp;
        if (FirebaseApp.getApps().isEmpty()) {
            notificationApp = FirebaseApp.initializeApp(firebaseOptions, "gentlemonster");
        } else {
            notificationApp = FirebaseApp.getInstance("gentlemonster");
        }
        return FirebaseMessaging.getInstance(notificationApp);
    }
}
