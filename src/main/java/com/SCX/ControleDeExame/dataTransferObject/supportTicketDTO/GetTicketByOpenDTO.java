package com.SCX.ControleDeExame.dataTransferObject.supportTicketDTO;

import com.SCX.ControleDeExame.domain.supportTicket.SupportTicket;
import lombok.Getter;

import java.util.UUID;

@Getter
public class GetTicketByOpenDTO {
    private String id;
    private String subject;
    private String message;
    private String response;

    public GetTicketByOpenDTO(UUID id, String subject, String message, String response) {
        this.id = id.toString();
        this.subject = subject;
        this.message = message;
        this.response = response;
    }
}