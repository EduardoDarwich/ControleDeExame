package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.adminSystemDTO.ResponseCliSystDTO;
import com.SCX.ControleDeExame.dataTransferObject.adminSystemDTO.ResponseLabSystDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.RequestCnpjClinicaDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseLabCliDTO;
import com.SCX.ControleDeExame.dataTransferObject.adminSystemDTO.ResponsePatSystDTO;
import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.LaboratoryVerificDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.RequestSecretaryEmailDTO;
import com.SCX.ControleDeExame.service.AdminSystemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adminSystem")
public class AdminSystemController {

    //Criando instancias utilizadas
    @Autowired
    AdminSystemService adminSystemService;

    //Rota para desativar uma clínica
    @PatchMapping("/disableClinic")
    public ResponseEntity disableClinic (@RequestBody @Valid RequestCnpjClinicaDTO data){
        adminSystemService.disableClinic(data);
        return ResponseEntity.ok().build();
    }

    //Rota para ativar uma clínica
    @PatchMapping("/enableClinic")
    public ResponseEntity enableClinic (@RequestBody @Valid RequestCnpjClinicaDTO data){
        adminSystemService.enableClinic(data);
        return ResponseEntity.ok().build();
    }

    //Rota para listar todas as clínicas do sistema
    @GetMapping("/getAllCli")
    public ResponseEntity<List<ResponseCliSystDTO>> getAllCli (){
        return ResponseEntity.ok(adminSystemService.listAllClinics());
    }

    //Rota para listar todos os laboratórios do sistema
    @GetMapping("/getAllLab")
    public ResponseEntity<List<ResponseLabSystDTO>> getAllLab (){
        return ResponseEntity.ok(adminSystemService.listAllLaboratory());
    }

    //Rota para listar todas os pacientes do sistema
    @GetMapping("/getAllPat")
    public ResponseEntity<List<ResponsePatSystDTO>> getAllPat (){
        return ResponseEntity.ok(adminSystemService.listAllPat());
    }

    //Rota para retornar quantos laboratórios estão cadastrados no sistema
    @GetMapping("/getCountLab")
    public ResponseEntity LabCount (){
        return ResponseEntity.ok(adminSystemService.countLabs());
    }

    //Rota para retornar quantas clínicas estão cadastradas no sistema
    @GetMapping("/getCountCli")
    public ResponseEntity CliCount (){
        return ResponseEntity.ok(adminSystemService.countClinic());
    }

    //Rota para retornar quantos pacientes estão cadastrados no sistema
    @GetMapping("/getCountPat")
    public ResponseEntity PatCount (){
        return ResponseEntity.ok(adminSystemService.countPats());
    }

    //Rota para ativar um usuário administrador de clínica
    @PatchMapping("/enableAdmCli")
    public ResponseEntity enableAcmCli (@RequestBody @Valid RequestSecretaryEmailDTO data){
        adminSystemService.enableAdmClinic(data);
        return ResponseEntity.ok().build();
    }

    //Rota para desativar um usuário adm de clínica
    @PatchMapping("/disableAdmCli")
    public ResponseEntity disableAcmCli (@RequestBody @Valid RequestSecretaryEmailDTO data){
        adminSystemService.disableAdmClinic(data);
        return ResponseEntity.ok().build();
    }

    //Rota para criar um usuario admSystem
    @PostMapping("/registerUser")
    public ResponseEntity registerUserAdm() {
        adminSystemService.registerFirstAdmin();

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    //Rota para desativar um laboratório do sistema
    @PatchMapping("/disableLabSyst")
    public ResponseEntity disableLabCli (@RequestBody @Valid LaboratoryVerificDTO data){
        adminSystemService.disableLab(data);
        return ResponseEntity.ok().build();
    }

    //Rota para ativar um laboratório do sistema
    @PatchMapping("/enableLabSyst")
    public ResponseEntity enableLabCli (@RequestBody @Valid LaboratoryVerificDTO data){
        adminSystemService.enableLab(data);
        return ResponseEntity.ok().build();
    }



}
