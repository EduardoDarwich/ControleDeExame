package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.RequestNameClinicDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.*;
import com.SCX.ControleDeExame.dataTransferObject.examsDTO.GetByDoctorDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsRequestDTO.ExamsRequestDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsTypeDTO.ExamsTypeDTO;
import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.LaboratoryRequestExamDTO;
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

    //Rota para registrar um medico
    @PostMapping("/register")
    public ResponseEntity register (@RequestBody @Valid CreateDoctorDTO data, @RequestHeader("Authorization") RequestTokenDTO dataT){
        UserDetails user =  authRepository.findByUsernameKey(data.email());
        Auth auth = (Auth) user;

         doctorService.registerDoctor(data, dataT);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //Rota para deletar um médico
    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete (@PathVariable UUID id){
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok().build();
    }

    //Rota para atualizar um médico
    @PostMapping("/update/{id}")
    public ResponseEntity update (@PathVariable UUID id, @RequestBody @Valid CreateDoctorDTO data){
        doctorService.updateDoctor(data, id);
        return ResponseEntity.ok().build();
    }

    //Rota para Devolver os exames vinculados a um médico
    /*@GetMapping("/get/{id}")
    public ResponseEntity<List<GetByDoctorDTO>> listById (@PathVariable("id") RequestTokenDTO data) {
        List<GetByDoctorDTO> exams = doctorService.getExamsByDoctor(data);
        return ResponseEntity.ok(exams);
    }*/

    //Rota para fazer uma requisição de exames
    @PostMapping("/requestExm")
    public ResponseEntity requestExam (@RequestBody @Valid ExamsRequestDTO data, @RequestHeader("Authorization")RequestTokenDTO dataT){
        doctorService.requestExams(data, dataT);
        return ResponseEntity.ok().build();
    }

    //Rota para verificar se o médico ja está cadastrado na clinica
    @PostMapping("/searchDoc")
    public ResponseEntity searchDoc (@RequestBody @Valid DoctorVerificDTO data, @RequestHeader("Authorization")RequestTokenDTO dataT){
        boolean response = doctorService.verificDocCli(data, dataT);
        return ResponseEntity.ok(response);
    }

    //Rota para devolver as requisições de exame pendente do medico da clinica especifica
    @GetMapping("/getRequestExamPendent")
    public ResponseEntity<List<DoctorRequestExamDTO>> requestExamLab (@RequestHeader("Authorization") RequestTokenDTO dataT){
        return ResponseEntity.ok(doctorService.doctorRequestExam(dataT));
    }

    //Rota para ver se o medico existe no sistema
    @PostMapping("/getByCrm")
    public ResponseEntity getByCrm (@RequestBody @Valid DoctorVerificDTO data){
        boolean exists= doctorService.doctorVerific(data);
        return ResponseEntity.ok(exists);
    }

    //Metodo para Retornar a consulta ativa do médico
    @GetMapping("/getAppointmentOpen")
    public ResponseEntity getAppointmentOpen (@RequestHeader("Authorization")RequestTokenDTO dataT){

        return ResponseEntity.ok(doctorService.returnOpenAppointment(dataT));
    }

    //Rota para cadastrar um medico que ja está cadastrado no sistema a uma clinica
    @PostMapping("/transferDoctor")
    public ResponseEntity transferDoctor(@RequestBody @Valid DoctorVerificDTO data, @RequestHeader("Authorization")RequestTokenDTO dataT){
        doctorService.registerDocUserExists(data, dataT);
        return ResponseEntity.ok().build();
    }

    //Rota para devolver as clinicas que o medico está cadastrado
    @GetMapping("/clinicsDoctor")
    public ResponseEntity<List<ResponseClinicDocDTO>> verifyClinicByDoctor(@RequestHeader("Authorization") RequestTokenDTO dataT){
        List<ResponseClinicDocDTO> clinics = doctorService.clinicsDoctor(dataT);
        return ResponseEntity.ok(clinics);
    }

    //Rota para alterar a clinica que o médico está "logado"
    @PatchMapping("/updateClinicDocPresent")
    public ResponseEntity updateClinicDoc(@RequestBody @Valid RequestNameClinicDTO data, @RequestHeader("Authorization") RequestTokenDTO dataT){
        doctorService.updateClinicMed(data, dataT);
        return ResponseEntity.ok().build();
    }

    //Rota para devolver o nome da clinica que o médico está "logado"
    @GetMapping("/getClinicActive")
    public ResponseEntity getClinicActive(@RequestHeader("Authorization") RequestTokenDTO dataT){
        return ResponseEntity.ok(doctorService.clinicDocActive(dataT));
    }

    //Rota para fechar uma consulta
    @PatchMapping("/closeAppointment")
    public ResponseEntity closeAppointment(@RequestHeader("Authorization") RequestTokenDTO dataT){
        doctorService.closeAppointment(dataT);
        return ResponseEntity.ok().build();
    }

    //Rota para retornar os laboratórios disponiveis na clinica que o medico está ativo
    @GetMapping("/getLabDocCli")
    public ResponseEntity<List<ResponseDocCliLabDTO>> findLabByDocCli(@RequestHeader("Authorization") RequestTokenDTO dataT){
        return ResponseEntity.ok(doctorService.LabByclinicDoc( dataT));
    }

    //Rota para listar todos os tipos de exame
    @GetMapping("/getExamsType")
    public ResponseEntity<List<ExamsTypeDTO>> examsTypes(){
        return ResponseEntity.ok(doctorService.getExamsType());
    }




}
