package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.anamnesisDTO.CalculatorBmiDTO;
import com.SCX.ControleDeExame.dataTransferObject.anamnesisDTO.CreateAnamnesisDTO;
import com.SCX.ControleDeExame.dataTransferObject.anamnesisDTO.CreateCustomFieldDTO;
import com.SCX.ControleDeExame.dataTransferObject.anamnesisDTO.ResultBmiDTO;
import com.SCX.ControleDeExame.dataTransferObject.appointmentDTO.ReturnAppointmentsPatDTO;
import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.RequestNameClinicDTO;
import com.SCX.ControleDeExame.dataTransferObject.consultationDTO.CloseConsultationDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.*;
import com.SCX.ControleDeExame.dataTransferObject.examsDTO.CreateExamDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsRequestDTO.ExamsRequestDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsRequestDTO.GetExamsRequestIdDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsTypeDTO.ExamsTypeDTO;
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

    //Criando instancias utilizadas
    @Autowired
    DoctorService doctorService;

    @Autowired
    AuthRepository authRepository;

    //Rota para registrar um médico
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

    //Rota para fazer uma requisição de exames
    @PostMapping("/requestExm")
    public ResponseEntity<GetExamsRequestIdDTO> requestExam (@RequestBody @Valid ExamsRequestDTO data, @RequestHeader("Authorization")RequestTokenDTO dataT){

        return ResponseEntity.ok(doctorService.requestExams(data, dataT));
    }

    //Rota para verificar se o médico já está cadastrado na clínica
    @PostMapping("/searchDoc")
    public ResponseEntity searchDoc (@RequestBody @Valid DoctorVerificDTO data, @RequestHeader("Authorization")RequestTokenDTO dataT){
        boolean response = doctorService.verificDocCli(data, dataT);
        return ResponseEntity.ok(response);
    }

    //Rota para devolver as requisições de exame pendente do médico na clínica específica
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

    //Rota para cadastrar um médico já cadastrado no sistema a uma clínica
    @PostMapping("/transferDoctor")
    public ResponseEntity transferDoctor(@RequestBody @Valid DoctorVerificDTO data, @RequestHeader("Authorization")RequestTokenDTO dataT){
        doctorService.registerDocUserExists(data, dataT);
        return ResponseEntity.ok().build();
    }

    //Rota para devolver as clínicas que o medico está cadastrado
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

    //Rota para retornar os laboratórios disponiveis na clínica que o médico está ativo
    @GetMapping("/getLabDocCli")
    public ResponseEntity<List<ResponseDocCliLabDTO>> findLabByDocCli(@RequestHeader("Authorization") RequestTokenDTO dataT){
        return ResponseEntity.ok(doctorService.LabByclinicDoc( dataT));
    }

    //Rota para listar todos os tipos de exame
    @GetMapping("/getExamsType")
    public ResponseEntity<List<ExamsTypeDTO>> examsTypes(){
        return ResponseEntity.ok(doctorService.getExamsType());
    }


    //Rota para o iniciar uma consulta
    @PostMapping("/openConsultation")
    public ResponseEntity openConsultation(@RequestHeader("Authorization") RequestTokenDTO dataT){
        doctorService.openConsultation(dataT);
        return ResponseEntity.ok().build();
    }

    //Rota para fechar uma consulta
    @PatchMapping("/closeConsultation")
    public ResponseEntity closeConsultation(@RequestHeader("Authorization") RequestTokenDTO dataT, @RequestBody @Valid CloseConsultationDTO data){
        doctorService.closeConsultation(dataT, data);
        return ResponseEntity.ok().build();
    }

    //Rota para registrar uma anamnese
    @PostMapping("/registerAnamnese")
    public ResponseEntity registerAnamnese(@RequestHeader("Authorization") RequestTokenDTO dataT, @RequestBody @Valid CreateAnamnesisDTO data){
        doctorService.registerNewAnamnese(dataT, data);
        return ResponseEntity.ok().build();
    }

    //Rota para calcular o imc
    @PostMapping("/bmiCalculator")
    public ResponseEntity<ResultBmiDTO> bmiCalculator(@RequestBody @Valid CalculatorBmiDTO data){
        return ResponseEntity.ok(doctorService.calculatorBmi(data));
    }

    //Rota para criar campos personalizados na anamnese
    @PostMapping("/createCustomField")
    public ResponseEntity createCustomField( @RequestHeader("Authorization") RequestTokenDTO dataT, @RequestBody @Valid List<CreateCustomFieldDTO> data){
        doctorService.createCustomField(dataT, data);
        return ResponseEntity.ok().build();
    }

    //Rota para Listar todos os atendimentos passados do paciente
    @GetMapping("/getAppointmentsPat")
    public ResponseEntity<List<ReturnAppointmentsPatDTO>> getAppointmentsPat (@RequestHeader("Authorization") RequestTokenDTO dataT){
        return ResponseEntity.ok(doctorService.getAppointmentPat(dataT));
    }

    //Rota para verificar se o médico está em consulta
    @GetMapping("/verifyDocIsConsult")
    public ResponseEntity verifyDocIsConsult( @RequestHeader("Authorization") RequestTokenDTO dataT){

        return ResponseEntity.ok(doctorService.verifyDocIsConsult(dataT));
    }

    //Rota para criar os exames
    @PostMapping("/createExams")
    public ResponseEntity createExams( @RequestHeader("Authorization") RequestTokenDTO dataT, @RequestBody @Valid List<CreateExamDTO> data){
        doctorService.createExam(dataT, data);
        return ResponseEntity.ok().build();
    }

    //Rota para desativar o medico
    @PatchMapping("/disableDoc")
    public ResponseEntity disableDoc( @RequestHeader("Authorization") RequestTokenDTO dataT){
        doctorService.disableDoc(dataT);
        return ResponseEntity.ok().build();
    }

    //Rota para tirar o medico de qualquer clinica ativa
    @PatchMapping("/setDocCliZero")
    public ResponseEntity setDocCliZero( @RequestHeader("Authorization") RequestTokenDTO dataT){
        doctorService.setCliZero(dataT);
        return ResponseEntity.ok().build();
    }
}
