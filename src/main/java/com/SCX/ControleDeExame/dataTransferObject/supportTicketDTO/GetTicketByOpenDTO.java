package com.SCX.ControleDeExame.dataTransferObject.supportTicketDTO;

import com.SCX.ControleDeExame.domain.supportTicket.SupportTicket;

public record GetTicketByOpenDTO(String id, String subject, String message, String response) {
    public GetTicketByOpenDTO(SupportTicket supportTicket){
        this(String.valueOf(supportTicket.getAuthId()), supportTicket.getSubject(), supportTicket.getMessage(), supportTicket.getResponse());
    }
}
