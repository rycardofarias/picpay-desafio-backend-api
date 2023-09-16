package com.example.picpay.services;

import com.example.picpay.dtos.NotificationDTO;
import com.example.picpay.entities.User;
import com.example.picpay.exceptions.NotificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationService {

    @Autowired
    private RestTemplate restTemplate;

    public void sendNotification(User user, String message) throws NotificationException {
        String email = user.getEmail();

        NotificationDTO notificationResquest = new NotificationDTO(email, message);

//        ResponseEntity<String> notificationResponse = restTemplate.postForEntity("http://o4d9z.mocklab.io/notify",notificationResquest, String.class);
//
//        if(!(notificationResponse.getStatusCode() == HttpStatus.OK)){
//            System.out.println("Erro ao enviar a notificação");
//            throw new NotificationException(ExceptionMessages.NOTIFICATION_SERVICE_UNAVAILABLE);
//        }

        System.out.println("Sucesso ao enviar a notificação");
    }
}
