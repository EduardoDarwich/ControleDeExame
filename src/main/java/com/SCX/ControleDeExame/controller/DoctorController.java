package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.DoctorDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsDTO.GetByDoctorDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsRequestDTO.ExamsRequestDTO;
import com.SCX.ControleDeExame.domain.doctor.Doctor;

import com.SCX.ControleDeExame.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    DoctorService doctorService;

    @PostMapping("/register")
    public ResponseEntity register (@RequestBody @Valid DoctorDTO data){
        Doctor doctor = doctorService.registerDoctor(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(doctor);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete (@PathVariable UUID id){
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update/{id}")
    public ResponseEntity update (@PathVariable UUID id, @RequestBody @Valid DoctorDTO data){
        doctorService.updateDoctor(data, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<List<GetByDoctorDTO>> listById (@PathVariable("id") RequestTokenDTO data) {
        List<GetByDoctorDTO> exams = doctorService.getExamsByDoctor(data);
        return ResponseEntity.ok(exams);
    }

    @PostMapping("/requestExm/{token}")
    public ResponseEntity requestExam (@RequestBody @Valid ExamsRequestDTO data,
                                       @PathVariable("token") @Valid RequestTokenDTO dataT
                                       ){
        doctorService.requestExams(data, dataT);
        return ResponseEntity.ok().build();
    }
}
