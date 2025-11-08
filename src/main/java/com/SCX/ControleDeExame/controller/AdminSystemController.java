package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.RequestCnpjClinicaDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseLabCliDTO;
import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.LaboratoryVerificDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.ResponsePatSystDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.RequestSecretaryEmailDTO;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.repository.ClinicRepository;
import com.SCX.ControleDeExame.service.AdminSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/adminSystem")
public class AdminSystemController {
    @Autowired
    AdminSystemService adminSystemService;

    //Rota para ativar uma clinica
    @PatchMapping("/disableClinic")
    public ResponseEntity disableClinic (RequestCnpjClinicaDTO data){
        adminSystemService.disableClinic(data);
        return ResponseEntity.ok().build();
    }

    //Rota para desativar uma clinica
    @PatchMapping("/enableClinic")
    public ResponseEntity enableClinic (RequestCnpjClinicaDTO data){
        adminSystemService.enableClinic(data);
        return ResponseEntity.ok().build();
    }

    //Rota para listar todas as clinicas do sistema
    @GetMapping("/getAllCli")
    public ResponseEntity<List<ResponseLabCliDTO>> getAllCli (){
        return ResponseEntity.ok(adminSystemService.listAllClinics());
    }

    //Rota para listar todos os laboratorios do sistema
    @GetMapping("/getAllLab")
    public ResponseEntity<List<ResponseLabCliDTO>> getAllLab (){
        return ResponseEntity.ok(adminSystemService.listAllLaboratory());
    }

    //Rota para listar todas os pacientes do sistema
    @GetMapping("/getAllPat")
    public ResponseEntity<List<ResponsePatSystDTO>> getAllPat (){
        return ResponseEntity.ok(adminSystemService.listAllPat());
    }

    //Rota para retornar quantos laboratorios tem cadastrado no sistema
    @GetMapping("/getCountLab")
    public ResponseEntity LabCount (){
        return ResponseEntity.ok(adminSystemService.countLabs());
    }

    //Rota para retornar quantos laboratorios tem cadastrado no sistema
    @GetMapping("/getCountCli")
    public ResponseEntity CliCount (){
        return ResponseEntity.ok(adminSystemService.countClinic());
    }

    //Rota para retornar quantos laboratorios tem cadastrado no sistema
    @GetMapping("/getCountPat")
    public ResponseEntity PatCount (){
        return ResponseEntity.ok(adminSystemService.countPats());
    }

    //Rota para ativar um usuario adm de clinica
    @PatchMapping("/enableAdmCli")
    public ResponseEntity enableAcmCli (RequestSecretaryEmailDTO data){
        adminSystemService.enableAdmClinic(data);
        return ResponseEntity.ok().build();
    }

    //Rota para ativar um usuario adm de clinica
    @PatchMapping("/disableAdmCli")
    public ResponseEntity disableAcmCli (RequestSecretaryEmailDTO data){
        adminSystemService.disableAdmClinic(data);
        return ResponseEntity.ok().build();
    }



}
