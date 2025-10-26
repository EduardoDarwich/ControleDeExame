package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.supportTicketDTO.CreateSupportTicketDTO;
import com.SCX.ControleDeExame.dataTransferObject.supportTicketDTO.ResponseSupportDTO;
import com.SCX.ControleDeExame.domain.supportTicket.SupportTicket;
import com.SCX.ControleDeExame.infra.security.TokenService;
import com.SCX.ControleDeExame.repository.AuthRepository;
import com.SCX.ControleDeExame.repository.SupportTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SupportTicketService {

    @Autowired
    SupportTicketRepository supportTicketRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    AuthRepository authRepository;

    //Metodo para criar um ticket de suporte (testar)
    public void createTicket (CreateSupportTicketDTO data, RequestTokenDTO dataT){

        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var auth = authRepository.findById(UUID.fromString(id));

        SupportTicket newSupportTicket = new SupportTicket();
        newSupportTicket.setSubject(data.subject());
        newSupportTicket.setMessage(data.message());
        supportTicketRepository.save(newSupportTicket);

    }

    //Metodo para responder um ticket de suporte (testar)
    public ResponseSupportDTO responseTicket (ResponseSupportDTO data, RequestTokenDTO dataT) {

        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var auth = authRepository.findById(UUID.fromString(id));

        SupportTicket supportTicket = new SupportTicket();
        supportTicket.setResponse(data.response());
        supportTicket.setFinished(true);
        supportTicketRepository.save(supportTicket);

        return new ResponseSupportDTO(supportTicket.getSubject(), supportTicket.getMessage(), supportTicket.getResponse());

    }

}
