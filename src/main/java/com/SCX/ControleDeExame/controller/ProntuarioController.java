package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.appointmentDTO.GetAppointmentIdDTO;
import com.SCX.ControleDeExame.dataTransferObject.prontuarioDTO.ResponseAnamnesisDTO;
import com.SCX.ControleDeExame.dataTransferObject.prontuarioDTO.ReturnDiagnosticDTO;
import com.SCX.ControleDeExame.dataTransferObject.prontuarioDTO.ReturnExamsRequestsDTO;
import com.SCX.ControleDeExame.dataTransferObject.prontuarioDTO.ReturnExamsResultsDTO;
import com.SCX.ControleDeExame.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prontuario")
public class ProntuarioController {

    @Autowired
    DoctorService doctorService;

    //Rota para devolver a anamnese da consulta referente ao atendimento do prontuario
    @GetMapping("/getAnamneseConsult")
    public ResponseEntity<ResponseAnamnesisDTO> getAnamneseConsult (@RequestBody @Valid GetAppointmentIdDTO data){
        return ResponseEntity.ok(doctorService.getAnamneseByConsult(data));
    }

    //Rota para retornar o diagnostico de uma consulta
    @GetMapping("/getDiagnostic")
    public ResponseEntity<ReturnDiagnosticDTO> getDiagnostic (@RequestBody @Valid GetAppointmentIdDTO data){
        return ResponseEntity.ok(doctorService.returnDiagnostic(data));
    }

    //Rota para retornar as requisições de exame de uma consulta
    @GetMapping("/getExamsRequest")
    public ResponseEntity<ReturnExamsRequestsDTO> getExamRequest (@RequestBody @Valid GetAppointmentIdDTO data){
        return ResponseEntity.ok(doctorService.returnExamsRequests(data));
    }

    //Rota para retornar os exames de uma consulta
    @GetMapping("/getExams")
    public ResponseEntity<ReturnExamsResultsDTO> getExams (@RequestBody @Valid GetAppointmentIdDTO data){
        return ResponseEntity.ok(doctorService.returnExamsResults(data));
    }

}
