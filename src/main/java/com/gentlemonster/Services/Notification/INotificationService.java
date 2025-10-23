package com.gentlemonster.Services.Notification;

import java.util.List;
import java.util.Map;

import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.Entities.Notification;
import com.gentlemonster.Exception.NotFoundException;

public interface INotificationService {
    PagingResponse<List<Notification>> getNotifications(Map<Object, String> filters);
    APIResponse<Notification> markReadNotification(String notificationId) throws NotFoundException; // Đánh dấu đã đọc
    APIResponse<Boolean> deleteNotification(String notificationId) throws NotFoundException;
    APIResponse<Boolean> readAllNotification(String token) throws NotFoundException;
}
