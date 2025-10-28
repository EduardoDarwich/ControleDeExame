package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.notificationDTO.GetIdNotDTO;
import com.SCX.ControleDeExame.dataTransferObject.notificationDTO.GetNotificationUnreadDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.notification.Notification;
import com.SCX.ControleDeExame.infra.security.TokenService;
import com.SCX.ControleDeExame.repository.AuthRepository;
import com.SCX.ControleDeExame.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    AuthRepository authRepository;

    //Metodo para enviar a notificação para o usuario
    public void send(Auth user, String title, String message) {
        var notification = new Notification();
        notification.setAuthId(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setReadFile(false);
        notificationRepository.save(notification);
    }

    //Metodo para marcar a notificação como lida
    public void markAsRead(GetIdNotDTO data) {
        System.out.println(data.id());
        Optional<Notification> notif = notificationRepository.findById(UUID.fromString(data.id()));
        notif.get().setReadFile (true);
        notificationRepository.save(notif.get());
    }

    //Metodo para listar as notificações não lidas
    public List<GetNotificationUnreadDTO> getUnread(RequestTokenDTO dataT) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var auth = authRepository.findById(UUID.fromString(id));
        return notificationRepository.findByOpen(auth.get());
    }



}
