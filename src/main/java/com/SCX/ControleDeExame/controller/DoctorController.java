package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.CreateDoctorDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.DoctorVerificDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.ResponseClinicDocDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsDTO.GetByDoctorDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsRequestDTO.ExamsRequestDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;

import com.SCX.ControleDeExame.repository.AuthRepository;
import com.SCX.ControleDeExame.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    DoctorService doctorService;

    @Autowired
    AuthRepository authRepository;

    @PostMapping("/register")
    public ResponseEntity register (@RequestBody @Valid CreateDoctorDTO data, @RequestHeader("Authorization") RequestTokenDTO dataT){
        UserDetails user =  authRepository.findByUsernameKey(data.email());
        Auth auth = (Auth) user;

         doctorService.registerDoctor(data, dataT);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete (@PathVariable UUID id){
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update/{id}")
    public ResponseEntity update (@PathVariable UUID id, @RequestBody @Valid CreateDoctorDTO data){
        doctorService.updateDoctor(data, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<List<GetByDoctorDTO>> listById (@PathVariable("id") RequestTokenDTO data) {
        List<GetByDoctorDTO> exams = doctorService.getExamsByDoctor(data);
        return ResponseEntity.ok(exams);
    }

    @PostMapping("/requestExm/{token}")
    public ResponseEntity requestExam (@RequestBody @Valid ExamsRequestDTO data, @PathVariable("token") @Valid RequestTokenDTO dataT){
        doctorService.requestExams(data, dataT);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/searchDoc")
    public ResponseEntity searchDoc (@RequestBody @Valid DoctorVerificDTO data, @RequestHeader("Authorization")RequestTokenDTO dataT){
        boolean response = doctorService.verificDocCli(data, dataT);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/getByCrm")
    public ResponseEntity getByCrm (@RequestBody @Valid DoctorVerificDTO data){
        boolean exists= doctorService.doctorVerific(data);
        return ResponseEntity.ok(exists);
    }

    @PostMapping("/transferDoctor")
    public ResponseEntity transferDoctor(@RequestBody @Valid DoctorVerificDTO data, @RequestHeader("Authorization")RequestTokenDTO dataT){
        doctorService.registerDocUserExists(data, dataT);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/clinicsDoctor")
    public ResponseEntity<List<ResponseClinicDocDTO>> verifyClinicByDoctor(@RequestHeader("Authorization") RequestTokenDTO dataT){
        List<ResponseClinicDocDTO> clinics = doctorService.clinicsDoctor(dataT);
        return ResponseEntity.ok(clinics);
    }


}
