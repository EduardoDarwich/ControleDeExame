package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.DoctorResultExamDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.CliPatDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.ExamsFileDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.PatientDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.PatientRequestExamDTO;
import com.SCX.ControleDeExame.domain.patient.Patient;
import com.SCX.ControleDeExame.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    PatientService patientService;


    //Rota para deletar um paciente
    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete (@PathVariable UUID id){
        patientService.deletePatient(id);
        return ResponseEntity.ok().build();
    }

    //Rota para Atualizar um paciente
    @PostMapping("/update/{id}")
    public ResponseEntity update (@PathVariable UUID id, @RequestBody @Valid PatientDTO data){
        patientService.updatePatient(data, id);
        return ResponseEntity.ok().build();
    }

    //Rota para devolver todos os pacientes do sistema
    @GetMapping("/GetAllPatient")
    public ResponseEntity getAll(){
        return ResponseEntity.ok(patientService.getAllPatient());

    }

    //Rota para devolver um paciente pelo Id
    @GetMapping("/GetPatientById")
    public ResponseEntity getById(@RequestBody @Valid RequestTokenDTO data){
        return ResponseEntity.ok(patientService.getPatientById(data));
    }

    //Rota para devolver as requisições de exame pendentes do paciente
    @GetMapping("/getRequestExamPendent")
    public ResponseEntity<List<PatientRequestExamDTO>> requestExamPat (@RequestHeader("Authorization") RequestTokenDTO dataT){
        return ResponseEntity.ok(patientService.requestExamPatient(dataT));
    }

    //Rota para devolver os exames do paciente
    @GetMapping("/getExamsResult")
    public ResponseEntity<List<ExamsFileDTO>> examsResult(@RequestHeader("Authorization") RequestTokenDTO dataT){
        return ResponseEntity.ok(patientService.patientResultExam(dataT));
    }

    //Rota para anonmizar os dados de um paciente
    @PatchMapping("/anonimizePat")
    public ResponseEntity anonimizePat (@RequestHeader("Authorization") RequestTokenDTO dataT){
        patientService.disablePat(dataT);
        return ResponseEntity.ok().build();
    }

    //Rota para devolver todas as clinicas do paciente
    @GetMapping("/getClinicPat")
    public ResponseEntity<List<CliPatDTO>> getCli (@RequestHeader("Authorization") RequestTokenDTO dataT){
        return ResponseEntity.ok(patientService.getCliPat(dataT));
    }

}
