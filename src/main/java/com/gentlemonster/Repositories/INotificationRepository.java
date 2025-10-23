package com.gentlemonster.Repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.gentlemonster.Entities.Notification;

public interface INotificationRepository extends JpaRepository<Notification, UUID>, JpaSpecificationExecutor<Notification>  {
    Page<Notification> findByContextContainingIgnoreCase(String name, Pageable pageable);
}
