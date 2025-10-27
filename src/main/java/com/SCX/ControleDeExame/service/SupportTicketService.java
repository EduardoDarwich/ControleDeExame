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

    //Metodo para criar um ticket de suporte (testar)
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

    //Metodo para responder um ticket de suporte (testar)
    public void responseTicket (GetTicketByOpenDTO data, RequestTokenDTO dataT) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var auth = authRepository.findById(UUID.fromString(id));
        SupportTicket supportTicket = supportTicketRepository.findByAuthId_Id(UUID.fromString(data.id()));
        String msg = "Respondeu o ticket " + supportTicket.getId();


        supportTicket.setResponse(data.response());
        supportTicket.setFinished(true);
        supportTicketRepository.save(supportTicket);
        logService.logAction(auth.get(), msg);



    }

    //Metodo para criar um usuario do suporte (testar)
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

    //Metodo para visualizar os tickets abertos (testar)
    public List<GetTicketByOpenDTO> getTicket () {

        return supportTicketRepository.findAll().stream().map(GetTicketByOpenDTO::new).toList();

    }

    //Metodo para visualizar os tickets do usuario(testar)
    public List<ResponseSupportDTO> getTicketByUser (RequestTokenDTO dataT){

        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var auth = authRepository.findById(UUID.fromString(id));

        return supportTicketRepository.findByUser(auth.get().getId());
    }


}
