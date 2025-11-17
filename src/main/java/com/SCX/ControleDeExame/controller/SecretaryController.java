package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.appointmentDTO.RegisterAppointmentDTO;
import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseDocCliConsultDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponsePatCliDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.GetPatientByCPFDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.PatientDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.ResponseSecretaryClinicDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.SecretaryDTO;
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

    //Rota para registar um paciente
    @PostMapping("/registerPatient")
    public ResponseEntity register(@RequestBody @Valid PatientDTO data, @RequestHeader("Authorization") RequestTokenDTO dataT) {
        secretaryService.registerPatient(data, dataT);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //Rota para verificar se o paciente está cadastrado na clinica
    @PostMapping("/verificPatCli")
    public ResponseEntity patVerifyCli(@RequestBody @Valid GetPatientByCPFDTO data, @RequestHeader("Authorization") RequestTokenDTO dataT) {

        return ResponseEntity.ok(secretaryService.patientCli(data, dataT));
    }

    //Rota para verificar se o paciente está cadastrado no sistema
    @PostMapping("/verificPatSyst")
    public ResponseEntity patVerificSyst(@RequestBody @Valid GetPatientByCPFDTO data) {
        return ResponseEntity.ok(secretaryService.patientExists(data));
    }

    //Rota para cadastrar um paciente que já existe no sistema
    @PostMapping("/transferPat")
    public ResponseEntity transferPat(@RequestBody @Valid GetPatientByCPFDTO data, @RequestHeader("Authorization") RequestTokenDTO dataT) {
        secretaryService.registerPatExistsCli(data, dataT);
        return ResponseEntity.ok().build();
    }

    //Rota que retorna a clinica da secretaria
    @GetMapping("/clinicSecretary")
    public ResponseEntity<ResponseSecretaryClinicDTO> clinicSecretary(@RequestHeader("Authorization") RequestTokenDTO dataT) {
        ResponseSecretaryClinicDTO response = secretaryService.clinicSecretary(dataT);
        return ResponseEntity.ok(response);
    }

    //Rota para deletar a secretaria
    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        secretaryService.deleteSecretary(id);
        return ResponseEntity.ok().build();
    }

    //Rota para atualizar a secretaria
    @PostMapping("/update/{id}")
    public ResponseEntity update(@PathVariable UUID id, @RequestBody @Valid SecretaryDTO data) {
        secretaryService.updateSecretary(data, id);
        return ResponseEntity.ok().build();
    }

    //Rota para devolver os pacientes cadastrados na clínica (mostrando o nome e o status de usuario)
    @GetMapping("/getPatientsCli")
    public ResponseEntity<List<ResponsePatCliDTO>> getPatients(@RequestHeader("Authorization") RequestTokenDTO dataT) {
        return ResponseEntity.ok(secretaryService.patCli(dataT));
    }

    //Rota para devolver os médicos disponíveis para consulta
    @GetMapping("/getDocsAvailable")
    public ResponseEntity<List<ResponseDocCliConsultDTO>> getDocsAvailable(@RequestHeader("Authorization") RequestTokenDTO dataT) {
        return ResponseEntity.ok(secretaryService.docCLiConsult(dataT));
    }

    //Rota para abrir um atendimento
    @PostMapping("/openAppointment")
    public ResponseEntity openAppointment(@RequestHeader("Authorization") RequestTokenDTO dataT, @RequestBody @Valid RegisterAppointmentDTO data) {
        secretaryService.registerAppointment(data, dataT);
        return ResponseEntity.ok().build();
    }


}
