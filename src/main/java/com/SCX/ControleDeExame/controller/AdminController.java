package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.adminDTO.CreateAdminDTO;
import com.SCX.ControleDeExame.dataTransferObject.adminDTO.ResponseAdminClinicDTO;
import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseDocCliDTO;
import com.SCX.ControleDeExame.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin")
@RestController
public class AdminController {

    @Autowired
    AdminService adminService;

    @PostMapping("/register")
    public ResponseEntity registerAdmin (@RequestBody @Valid CreateAdminDTO data, @RequestHeader("Authorization") RequestTokenDTO token){
        adminService.registerAdm(data,token);
        return  ResponseEntity.ok().build();
    }

    @GetMapping("/clinicAdm")
    public ResponseEntity<ResponseAdminClinicDTO> clinicAdm (@RequestHeader("Authorization") RequestTokenDTO dataT ){
        ResponseAdminClinicDTO response = adminService.clinicAdm(dataT);
        return  ResponseEntity.ok(response);
    }

    @GetMapping("/doctorClinic")
    public ResponseEntity<List<ResponseDocCliDTO>> docCli (@RequestHeader ("Authorization") RequestTokenDTO dataT){
        return ResponseEntity.ok(adminService.docCli(dataT));
    }

}
