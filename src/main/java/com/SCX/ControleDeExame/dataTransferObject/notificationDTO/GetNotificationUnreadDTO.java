package com.SCX.ControleDeExame.dataTransferObject.notificationDTO;

import lombok.Getter;

import java.util.UUID;

@Getter
public class GetNotificationUnreadDTO {
    private String id;
    private String title;
    private String message;
    private String readFile;

    public GetNotificationUnreadDTO(UUID id, String title, String message, boolean readFile){
        this.id = id.toString();
        this.title = title;
        this.message = message;
        this.readFile = readFile ? "Lido" : "Não lido";
    }
}
