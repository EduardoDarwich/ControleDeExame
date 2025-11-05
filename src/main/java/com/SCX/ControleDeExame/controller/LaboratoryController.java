
package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.fileDTO.UploadDTO;
import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.*;
import com.SCX.ControleDeExame.service.ClinicService;
import com.SCX.ControleDeExame.service.ExamsFileService;
import com.SCX.ControleDeExame.service.LaboratoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/laboratory")
public class LaboratoryController {

    @Autowired
    LaboratoryService laboratoryService;
    @Autowired
    ClinicService clinicService;
    @Autowired
    ExamsFileService examsFileService;

    //Rota para registrar um laboratorio
    @PostMapping("/register")
    public ResponseEntity register (@RequestBody @Valid CreateLaboratoryDTO data, @RequestHeader("Authorization") RequestTokenDTO dataT){
         clinicService.registerNewLaboratory(data, dataT);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //Rota para registrar um administrador do laboratório
    @PostMapping("/register/Adm")
    public ResponseEntity registerAdm (@RequestBody @Valid CreateLabUserAdmDTO data){
        laboratoryService.registerUserAdminLab(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //Rota para registar o usuario do comun do laboratorio
    @PostMapping("/register/User")
    public ResponseEntity registerUserLab (@RequestBody @Valid CreateLabUserDTO data, @RequestHeader("Authorization") RequestTokenDTO dataT){
        laboratoryService.registerUserLab(data, dataT);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    //Rota para deletar uma laboratorio
    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete (@PathVariable UUID id){
        laboratoryService.deleteLaboratory(id);
        return ResponseEntity.ok().build();
    }

    //Rota para listar as clinicas que o laboratorio está cadastrado
    @GetMapping("/clinicsLab")
    public ResponseEntity<List<ResponseClinicLabDTO>> verifyClinicByLab(@RequestHeader("Authorization") RequestTokenDTO dataT){
        return ResponseEntity.ok(laboratoryService.clinicsLaboratory(dataT));
    }

    //Rota para listar as requisições de exame que o laboratorio tem e estão pendentes
    @GetMapping("/getRequestExamPendent")
    public ResponseEntity<List<LaboratoryRequestExamDTO>> requestExamLab (@RequestHeader("Authorization") RequestTokenDTO dataT){
        return ResponseEntity.ok(laboratoryService.laboratoryRequestExam(dataT));
    }

    //Rota para realizar o upload do resultado do exame (testar)
    @PostMapping("/uploadExam")
    public ResponseEntity registerExam (@RequestBody @Valid UploadDTO data, @RequestHeader("Authorization") RequestTokenDTO dataT) throws IOException {
        examsFileService.uploadFile(data, dataT);
        return ResponseEntity.ok().build();
    }
}