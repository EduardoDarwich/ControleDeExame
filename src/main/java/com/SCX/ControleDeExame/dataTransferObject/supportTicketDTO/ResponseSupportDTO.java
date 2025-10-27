package com.SCX.ControleDeExame.dataTransferObject.supportTicketDTO;

import com.SCX.ControleDeExame.domain.supportTicket.SupportTicket;

import java.util.UUID;

public class ResponseSupportDTO {
    private UUID id;
    private String subject;
    private String message;
    private String response;
    private String finished;

    public ResponseSupportDTO (UUID id, String subject, String message, String response, boolean finished){
        this.id = id;
        this.subject = subject;
        this.message = message;
        this.response = response;
        this.finished = finished ? "Encerrado" : "Aberto";
    }

}
