package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.GetAllPatientDTO;
import com.SCX.ControleDeExame.dataTransferObject.supportTicketDTO.CreateSupportTicketDTO;
import com.SCX.ControleDeExame.dataTransferObject.supportTicketDTO.GetTicketByOpenDTO;
import com.SCX.ControleDeExame.dataTransferObject.supportTicketDTO.ResponseSupportDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.role.Role;
import com.SCX.ControleDeExame.domain.supportTicket.SupportTicket;
import com.SCX.ControleDeExame.infra.security.TokenService;
import com.SCX.ControleDeExame.repository.AuthRepository;
import com.SCX.ControleDeExame.repository.RoleRepository;
import com.SCX.ControleDeExame.repository.SupportTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SupportTicketService {

    @Autowired
    SupportTicketRepository supportTicketRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    AuthRepository authRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    LogService logService;

    //Metodo para criar um ticket de suporte
    public void createTicket (CreateSupportTicketDTO data, RequestTokenDTO dataT){

        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var auth = authRepository.findById(UUID.fromString(id));

        SupportTicket newSupportTicket = new SupportTicket();
        newSupportTicket.setSubject(data.subject());
        newSupportTicket.setMessage(data.message());
        newSupportTicket.setAuthId(auth.get());
        supportTicketRepository.save(newSupportTicket);

        logService.logAction(auth.get(), "Abriu um ticket para o suporte");

    }

    //Metodo para responder um ticket de suporte
    public void responseTicket (GetTicketByOpenDTO data, RequestTokenDTO dataT) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var auth = authRepository.findById(UUID.fromString(id));
        System.out.println("sadasdas" + data.getId());
        Optional<SupportTicket> supportTicket = supportTicketRepository.findById(UUID.fromString(data.getId()));
        String msg = "Respondeu o ticket " + supportTicket.get().getId();


        supportTicket.get().setResponse(data.getResponse());
        supportTicket.get().setFinished(true);
        supportTicketRepository.save(supportTicket.get());
        logService.logAction(auth.get(), msg);



    }

    //Metodo para criar um usuario do suporte
    public void registerSupportUser(){
        Role userSupport = roleRepository.findByName("Support");
        String senha = "123456789";
        String encryptedPassword = new BCryptPasswordEncoder().encode(senha);

        Auth newAuth = new Auth();
        newAuth.setPassword_key(encryptedPassword);
        newAuth.setUsernameKey("suporte@gmail.com");
        newAuth.setName("vinicius");
        newAuth.setActive(true);
        newAuth.getRoles().add(userSupport);
        authRepository.save(newAuth);



    }

    //Metodo para visualizar os tickets abertos
    public List<GetTicketByOpenDTO> getTicket () {

        return supportTicketRepository.findByFinishedFalse();

    }

    //Metodo para visualizar os tickets do usuario
    public List<ResponseSupportDTO> getTicketByUser (RequestTokenDTO dataT){

        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var auth = authRepository.findById(UUID.fromString(id));

        return supportTicketRepository.findByUser(auth.get().getId());
    }


}
