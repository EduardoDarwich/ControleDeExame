package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.PatientDTO;
import com.SCX.ControleDeExame.dataTransferObject.supportTicketDTO.CreateSupportTicketDTO;
import com.SCX.ControleDeExame.dataTransferObject.supportTicketDTO.GetTicketByOpenDTO;
import com.SCX.ControleDeExame.dataTransferObject.supportTicketDTO.ResponseSupportDTO;
import com.SCX.ControleDeExame.service.SupportTicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/support")
public class SupportController {

    @Autowired
    SupportTicketService supportTicketService;

    //Rota para criar um pedido de suporte
    @PostMapping("/registerTicket")
    public ResponseEntity registerTicket(@RequestBody @Valid CreateSupportTicketDTO data, @RequestHeader("Authorization") RequestTokenDTO dataT) {
        supportTicketService.createTicket(data, dataT);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //Rota para responder um pedido de suporte
    @PatchMapping("/responseTicket")
    public ResponseEntity responseTicket(@RequestBody @Valid GetTicketByOpenDTO data, @RequestHeader("Authorization") RequestTokenDTO dataT) {
        supportTicketService.responseTicket(data, dataT);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //Rota para criar um usuario do suporte
    @PostMapping("/registerUser")
    public ResponseEntity registerUserSup() {
        supportTicketService.registerSupportUser();

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    //Rota para mostrar os tickets dos usuarios
    @GetMapping("/getByUser")
    public ResponseEntity<List<ResponseSupportDTO>> getTicketByUser (@RequestHeader("Authorization") RequestTokenDTO dataT){
        return ResponseEntity.ok(supportTicketService.getTicketByUser(dataT));
    }

    //Rota para mostrar todos os tickets abertos
    @GetMapping("/getOpen")
    public ResponseEntity<List<GetTicketByOpenDTO>> getTicketByOpen (){
        return ResponseEntity.ok(supportTicketService.getTicket());
    }
}
