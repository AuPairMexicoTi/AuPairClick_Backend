package com.aupair.aupaircl.model.notfication;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findAllByUser_EmailAndUser_IsLocked(String email, boolean locked);
}
