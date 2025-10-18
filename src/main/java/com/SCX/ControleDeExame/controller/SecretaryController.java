package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.PatientDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.SecretaryDTO;
import com.SCX.ControleDeExame.domain.secretary.Secretary;
import com.SCX.ControleDeExame.service.AdminService;
import com.SCX.ControleDeExame.service.SecretaryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/secretary")
public class SecretaryController {
    @Autowired
    SecretaryService secretaryService;

    @Autowired
    AdminService adminService;

    @PostMapping("/register")
    public ResponseEntity register (@RequestBody @Valid SecretaryDTO data){
        Secretary secretary = adminService.registerSecretary(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(secretary);
    }

    @PostMapping("/register")
    public ResponseEntity register (@RequestBody @Valid PatientDTO data, @RequestHeader("Authorization")RequestTokenDTO dataT){
        secretaryService.registerPatient(data, dataT);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete (@PathVariable UUID id){
        secretaryService.deleteSecretary(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update/{id}")
    public ResponseEntity update (@PathVariable UUID id, @RequestBody @Valid SecretaryDTO data){
        secretaryService.updateSecretary(data, id);
        return ResponseEntity.ok().build();
    }
}
