
package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.CreateLabUserAdmDTO;
import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.CreateLabUserDTO;
import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.CreateLaboratoryDTO;
import com.SCX.ControleDeExame.service.ClinicService;
import com.SCX.ControleDeExame.service.LaboratoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/laboratory")
public class LaboratoryController {

    @Autowired
    LaboratoryService laboratoryService;
    @Autowired
    ClinicService clinicService;

    @PostMapping("/register")
    public ResponseEntity register (@RequestBody @Valid CreateLaboratoryDTO data, @RequestHeader("Authorization") RequestTokenDTO dataT){
         clinicService.registerNewLaboratory(data, dataT);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/register/Adm")
    public ResponseEntity registerAdm (@RequestBody @Valid CreateLabUserAdmDTO data){
        laboratoryService.registerUserAdminLab(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("register/User")
    public ResponseEntity registerUserLab (@RequestBody @Valid CreateLabUserDTO data, @RequestHeader("Authorization") RequestTokenDTO dataT){
        laboratoryService.registerUserLab(data, dataT);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete (@PathVariable UUID id){
        laboratoryService.deleteLaboratory(id);
        return ResponseEntity.ok().build();
    }
}