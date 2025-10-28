package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.dataTransferObject.notificationDTO.GetNotificationUnreadDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    @Query("""
                select new com.SCX.ControleDeExame.dataTransferObject.notificationDTO.GetNotificationUnreadDTO(
                    n.id, n.title, n.message, n.readFile
                )
                FROM Notification n
                WHERE n.readFile = false and n.authId = :userId
            """)
    List<GetNotificationUnreadDTO> findByOpen(Auth userId);
}
