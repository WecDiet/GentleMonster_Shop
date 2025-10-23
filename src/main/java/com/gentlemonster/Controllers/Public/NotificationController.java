package com.gentlemonster.Controllers.Public;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gentlemonster.Contants.Enpoint;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.Entities.Notification;
import com.gentlemonster.Services.Firebase.FirebaseMessagingService;
import com.gentlemonster.Services.Notification.NotificationService;
import com.gentlemonster.Utils.JwtTokenUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RequestMapping(Enpoint.Notification.BASE)
@RestController
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private FirebaseMessagingService firebaseMessagingService;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @GetMapping()
    public ResponseEntity<PagingResponse<?>> getNotifications(@RequestParam Map<Object, String> filters){
        try {
            PagingResponse<List<Notification>> items = notificationService.getNotifications(filters);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new PagingResponse<>(null, List.of(e.getMessage()), 0, 0L));
        }
    }

    @PostMapping(Enpoint.Notification.MARK_READ)
    public ResponseEntity<APIResponse<?>> markReadNotification(@PathVariable String notificationId) {
        try {
            APIResponse<Notification> response = notificationService.markReadNotification(notificationId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new APIResponse<>(null, List.of(e.getMessage())));
        }
    }
    
    
    
}
