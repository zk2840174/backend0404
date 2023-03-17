package org.zerock.backend.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;

@Component
@Log4j2
public class FcmUtil {

    @Value("${fcm.key.file}")
    private String keyPath;

    private FirebaseApp  firebaseApp;


    @PostConstruct
    public void init() throws Exception{

        FileInputStream serviceAccount =
                new FileInputStream(keyPath);

        log.info("----------------------");
        log.info(serviceAccount);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        firebaseApp = FirebaseApp.initializeApp(options);
    }

    public void sendNotification(String token, String title, String body) throws Exception {

        Notification notification = Notification.builder().setTitle(title).setBody(body).build();

        Message message = Message.builder()
                .setNotification(notification)
                .setToken(token)
                .putData("route", "AAAA")
                .build();

        String response = FirebaseMessaging.getInstance().send(message);

        log.info("response: " + response);

    }

}
