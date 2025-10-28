package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.notificationDTO.GetIdNotDTO;
import com.SCX.ControleDeExame.dataTransferObject.notificationDTO.GetNotificationUnreadDTO;
import com.SCX.ControleDeExame.dataTransferObject.supportTicketDTO.GetTicketByOpenDTO;
import com.SCX.ControleDeExame.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @GetMapping("/getNoRead")
    public ResponseEntity<List<GetNotificationUnreadDTO>> getNoRead(@RequestHeader("Authorization") RequestTokenDTO dataT) {

        return ResponseEntity.ok(notificationService.getUnread(dataT));
    }

    @PatchMapping("/markRead")
    public ResponseEntity markRead(@RequestBody @Valid GetIdNotDTO data){
        notificationService.markAsRead(data);

        return ResponseEntity.ok().build();
    }

}
