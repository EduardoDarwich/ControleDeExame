package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
}
