package com.gentlemonster.Services.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.gentlemonster.Contants.MessageKey;
import com.gentlemonster.DTO.Responses.APIResponse;
import com.gentlemonster.DTO.Responses.PagingResponse;
import com.gentlemonster.Entities.Notification;
import com.gentlemonster.Exception.NotFoundException;
import com.gentlemonster.Repositories.IAuthRepository;
import com.gentlemonster.Repositories.INotificationRepository;
import com.gentlemonster.Repositories.IUserRepository;
import com.gentlemonster.Repositories.Specification.NotificationSpecification;
import com.gentlemonster.Utils.JwtTokenUtils;
import com.gentlemonster.Utils.LocalizationUtils;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {
    @Autowired
    private INotificationRepository notificationRepository;
    @Autowired
    private IAuthRepository authRepository;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private LocalizationUtils localizationUtils;
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    
    
    
    @Override
    public PagingResponse<List<Notification>> getNotifications(Map<Object, String> filters) {
        int page = Integer.parseInt(filters.getOrDefault("page","-1"));
        int limit = Integer.parseInt(filters.getOrDefault("limit","-1"));
        String order = filters.get("order");
        if (page == -1 && limit == -1) {
            Specification<Notification> notificationSpecification = NotificationSpecification.filtersNotification(filters.get("userId"), filters.get("search"));
            List<Notification> notifications = notificationRepository.findAll(notificationSpecification, Sort.by(Sort.Direction.DESC, "createdAt"));
            int totalNew = (int) notifications.stream()
                                              .filter(notification -> notification.getIsRead() == 0)
                                              .count();
            List<String> messages = new ArrayList<>();
            messages.add(localizationUtils.getLocalizedMessage(MessageKey.NOTIFICATION_GET_LIST_SUCCESS));
            return new PagingResponse<>(notifications, messages, 1, (long) notifications.size(), totalNew);
        }

        page = Math.max(Integer.parseInt(filters.getOrDefault("page","-1")), 1) - 1;
        Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        pageable = getPageable(pageable, page, limit, order);
        Specification<Notification> notificationSpecification = NotificationSpecification.filtersNotification(filters.get("userId"), filters.get("search"));
        Page<Notification> notifications = notificationRepository.findAll(notificationSpecification, pageable);
        int totalNew = (int) notifications.stream()
                                          .filter(notification -> notification.getIsRead() == 0)
                                          .count();
        List<String> messages = new ArrayList<>();
        messages.add(localizationUtils.getLocalizedMessage(MessageKey.NOTIFICATION_GET_LIST_SUCCESS));
        return new PagingResponse<>(notifications.getContent(), messages, notifications.getTotalPages(), notifications.getTotalElements(), totalNew);
    }

    @Override
    @Transactional
    public APIResponse<Notification> markReadNotification(String notificationId) throws NotFoundException {
        Notification notification = notificationRepository.findById(UUID.fromString(notificationId)).orElseThrow(() -> new NotFoundException(localizationUtils.getLocalizedMessage(MessageKey.NOTIFICATION_NOT_FOUND)));

        notification.setIsRead(1);
        notificationRepository.save(notification);

        return new APIResponse<>(notification, List.of(localizationUtils.getLocalizedMessage(MessageKey.NOTIFICATION_READ)));
    }

    @Override
    public APIResponse<Boolean> deleteNotification(String notificationId) throws NotFoundException{
        // Notification notification = notificationRepository.findById(UUID.fromString(notificationId)).orElseThrow(() -> new NotFoundException(localizationUtils.getLocalizedMessage(MessageKey.NOTIFICATION_NOT_FOUND)));

        // notificationRepository.delete(notification);
        // return new APIResponse<>(true, List.of(localizationUtils.getLocalizedMessage(MessageKey.NOTIFICATION_DELETE)));
        throw new UnsupportedOperationException("Unimplemented method 'deleteNotification'");
    }

    @Override
    public APIResponse<Boolean> readAllNotification(String token) throws NotFoundException{
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readAllNotification'");
    }
    

    private Pageable getPageable(Pageable pageable, int page, int limit, String order) {
    if (StringUtils.hasText(order)) {
        String[] orderParams = order.split(" ");
            if (orderParams.length == 2) {
                Sort.Direction direction = orderParams[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
                pageable = PageRequest.of(page, limit, Sort.by(new Sort.Order(direction, orderParams[0])));
            }
    }
    return pageable;
    }
}
