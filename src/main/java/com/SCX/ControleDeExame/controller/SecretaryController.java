package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.adminDTO.ResponseAdminClinicDTO;
import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.RequestNameClinicDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseDocCliConsultDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponsePatCliDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.GetPatientByCPFDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.PatientDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.ResponseSecretaryClinicDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.SecretaryDTO;
import com.SCX.ControleDeExame.domain.secretary.Secretary;
import com.SCX.ControleDeExame.service.AdminService;
import com.SCX.ControleDeExame.service.SecretaryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/secretary")
public class SecretaryController {
    @Autowired
    SecretaryService secretaryService;




    @PostMapping("/registerPatient")
    public ResponseEntity register (@RequestBody @Valid PatientDTO data, @RequestHeader("Authorization")RequestTokenDTO dataT){
        secretaryService.registerPatient(data, dataT);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/verificPatCli")
    public ResponseEntity patVerifyCli (@RequestBody @Valid GetPatientByCPFDTO data, @RequestHeader("Authorization") RequestTokenDTO dataT){

        return ResponseEntity.ok(secretaryService.patientCli(data, dataT));
    }

    @PostMapping("/verificPatSyst")
    public ResponseEntity patVerificSyst(@RequestBody @Valid GetPatientByCPFDTO data){
        return  ResponseEntity.ok(secretaryService.patientExists(data));
    }

    @PostMapping("/transferPat")
    public ResponseEntity transferPat(@RequestBody @Valid GetPatientByCPFDTO data, @RequestHeader("Authorization") RequestTokenDTO dataT){
        secretaryService.registerPatExistsCli(data, dataT);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/clinicSecretary")
    public ResponseEntity<ResponseSecretaryClinicDTO> clinicSecretary (@RequestHeader("Authorization") RequestTokenDTO dataT ){
        ResponseSecretaryClinicDTO response = secretaryService.clinicSecretary(dataT);
        return  ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete (@PathVariable UUID id){
        secretaryService.deleteSecretary(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update/{id}")
    public ResponseEntity update (@PathVariable UUID id, @RequestBody @Valid SecretaryDTO data){
        secretaryService.updateSecretary(data, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getPatientsCli")
    public ResponseEntity<List<ResponsePatCliDTO>> getPatients (@RequestHeader("Authorization") RequestTokenDTO dataT){
        return ResponseEntity.ok(secretaryService.patCli(dataT));
    }

    @GetMapping("/getDocsAvailable")
    public ResponseEntity<List<ResponseDocCliConsultDTO>> getDocsAvailable(@RequestHeader("Authorization") RequestTokenDTO dataT){
        return ResponseEntity.ok(secretaryService.docCLiConsult(dataT));
    }


}
