package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.adminDTO.CreateAdminDTO;
import com.SCX.ControleDeExame.dataTransferObject.adminDTO.ResponseAdminClinicDTO;
import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseDocCliDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseLabCliDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseSecretaryCliDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.DoctorVerificDTO;
import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.LaboratoryVerificDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.RequestSecretaryCpfDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.RequestSecretaryEmailDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.SecretaryDTO;
import com.SCX.ControleDeExame.domain.secretary.Secretary;
import com.SCX.ControleDeExame.exception.CpfExistException;
import com.SCX.ControleDeExame.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin")
@RestController
public class AdminController {

    @Autowired
    AdminService adminService;

    //Rota para registrar um administrador
    @PostMapping("/registerAdmin")
    public ResponseEntity registerAdmin(@RequestBody @Valid CreateAdminDTO data, @RequestHeader("Authorization") RequestTokenDTO token) {

        adminService.registerAdm(data, token);
        return ResponseEntity.ok().build();

    }

    //Rota para registrar uma secretaria
    @PostMapping("/registerSecretary")
    public ResponseEntity registerSecretary(@RequestBody @Valid SecretaryDTO data, @RequestHeader("Authorization") RequestTokenDTO dataT) {
        adminService.registerSecretary(data, dataT);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //Rota para verificar se o usuario da secretaria ja está cadastrado no sistema
    @PostMapping("/verificSecretaryExists")
    public ResponseEntity verificSecretary(@RequestBody @Valid RequestSecretaryCpfDTO data) {
        return ResponseEntity.ok(adminService.secretaryExists(data));
    }

    //Rota para devolver a clinica do administrador logado
    @GetMapping("/clinicAdm")
    public ResponseEntity<ResponseAdminClinicDTO> clinicAdm(@RequestHeader("Authorization") RequestTokenDTO dataT) {
        ResponseAdminClinicDTO response = adminService.clinicAdm(dataT);
        return ResponseEntity.ok(response);
    }

    //Rota para devolver os médicos cadastrados na clinica do administrador
    @GetMapping("/doctorClinic")
    public ResponseEntity<List<ResponseDocCliDTO>> docCli(@RequestHeader("Authorization") RequestTokenDTO dataT) {
        return ResponseEntity.ok(adminService.docCli(dataT));
    }

    //Rota para verificar se o laboratorio ja está na clinica do administrador
    @PostMapping("/verificLabCli")
    public ResponseEntity verificLabCli(@RequestHeader("Authorization") RequestTokenDTO dataT, @RequestBody @Valid LaboratoryVerificDTO data) {
        return ResponseEntity.ok(adminService.verificLabCLi(dataT, data));
    }

    //Rota para verificar se o laboratório existe no sistema
    @PostMapping("/verificLabExists")
    public ResponseEntity verificLabSyst(@RequestBody @Valid LaboratoryVerificDTO data) {
        return ResponseEntity.ok(adminService.labVerific(data));
    }

    //Rota para cadastrar um laboratório que ja existe no sistema mas não está cadastrado na clinica
    @PostMapping("/transferLab")
    public ResponseEntity transferLab(@RequestBody @Valid LaboratoryVerificDTO data, @RequestHeader("Authorization") RequestTokenDTO dataT) {
        adminService.registerLabExists(data, dataT);
        return ResponseEntity.ok().build();
    }

    //Rota para listar todos os laboratórios da clinica do administrador
    @GetMapping("/getLabCli")
    public ResponseEntity<List<ResponseLabCliDTO>> getLabCli(@RequestHeader("Authorization") RequestTokenDTO dataT) {
        return ResponseEntity.ok(adminService.labCli(dataT));
    }

    //Rota para listar todas as secretárias da clinica
    @GetMapping("/getSecretary")
    public ResponseEntity<List<ResponseSecretaryCliDTO>> getSecretaryCli(@RequestHeader("Authorization") RequestTokenDTO dataT) {
        return ResponseEntity.ok(adminService.responseSecretaryCli(dataT));
    }

    //Rota para desativar um usuario da secretaria
    @PatchMapping("/disableSecretary")
    public ResponseEntity disableSecretary(@RequestBody @Valid RequestSecretaryEmailDTO data) {
        adminService.disableSecretary(data);
        return ResponseEntity.ok().build();
    }

    //Rota para ativar um usuario da secretaria
    @PatchMapping("/enableSecretary")
    public ResponseEntity enableSecretary(@RequestBody @Valid RequestSecretaryEmailDTO data) {
        adminService.enableSecretary(data);
        return ResponseEntity.ok().build();
    }

    //Rota para desvincular um laboratorio
    @PatchMapping("/disableLaboratory")
    public ResponseEntity disableLaboratory(@RequestHeader("Authorization") RequestTokenDTO dataT, @RequestBody @Valid LaboratoryVerificDTO data) {
        adminService.disableLaboratory(dataT, data);
        return ResponseEntity.ok().build();
    }

    //Rota para ativar um laboratorio
    @PatchMapping("/enableLaboratory")
    public ResponseEntity enableLaboratory(@RequestHeader("Authorization") RequestTokenDTO dataT, @RequestBody @Valid LaboratoryVerificDTO data) {
        adminService.enableLaboratory(dataT, data);
        return ResponseEntity.ok().build();
    }

    //Rota para desativar um usuario do laboratorio
    @PatchMapping("/disableUserLab")
    public ResponseEntity disableuserLab(@RequestBody @Valid RequestSecretaryEmailDTO data) {
        adminService.disableLabUser(data);
        return ResponseEntity.ok().build();
    }

    //Rota para ativar um usuario do laboratorio
    @PatchMapping("/enableUserLab")
    public ResponseEntity enableuserLab(@RequestBody @Valid RequestSecretaryEmailDTO data) {
        adminService.enableLabUser(data);
        return ResponseEntity.ok().build();
    }

    //Rota para desvincular um medico
    @PatchMapping("/disableDoctor")
    public ResponseEntity disableDoctor(@RequestHeader("Authorization") RequestTokenDTO dataT, @RequestBody @Valid DoctorVerificDTO data) {
        adminService.disableDocCli(dataT, data);
        return ResponseEntity.ok().build();
    }


}
