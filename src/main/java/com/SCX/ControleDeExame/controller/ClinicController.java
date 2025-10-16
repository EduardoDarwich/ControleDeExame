package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.adminDTO.CreateFirstAdmDTO;
import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.CreateClinicAdminDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.CreateClinicDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.RequestCnpjClinica;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.DoctorVerificDTO;
import com.SCX.ControleDeExame.repository.ClinicRepository;
import com.SCX.ControleDeExame.service.ClinicService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clinic")
public class ClinicController {
    @Autowired
    ClinicService clinicService;


    @PostMapping("/create")
    public ResponseEntity createClinic (@RequestBody @Valid CreateClinicDTO data){
        clinicService.regiterClinic(data);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/firstAdm")
    public ResponseEntity createFirstAdm (@RequestBody @Valid CreateFirstAdmDTO data){

        clinicService.createFirstAdmin(data);

        return ResponseEntity.ok().build();
    }


}
