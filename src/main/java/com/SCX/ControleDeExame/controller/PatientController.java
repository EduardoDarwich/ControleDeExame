package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.PatientDTO;
import com.SCX.ControleDeExame.domain.patient.Patient;
import com.SCX.ControleDeExame.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    PatientService patientService;

    @PostMapping("/register")
    public ResponseEntity register (@RequestBody @Valid PatientDTO data){
         patientService.registerPatient(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete (@PathVariable UUID id){
        patientService.deletePatient(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update/{id}")
    public ResponseEntity update (@PathVariable UUID id, @RequestBody @Valid PatientDTO data){
        patientService.updatePatient(data, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/GetAllPatient")
    public ResponseEntity getAll(){
        return ResponseEntity.ok(patientService.getAllPatient());

    }


    @GetMapping("/GetPatientById")
    public ResponseEntity getById(@RequestBody @Valid RequestTokenDTO data){
        return ResponseEntity.ok(patientService.getPatientById(data));
    }
}
