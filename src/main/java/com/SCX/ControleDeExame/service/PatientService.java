package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.DoctorResultExamDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.*;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.patient.Patient;
import com.SCX.ControleDeExame.domain.role.Role;
import com.SCX.ControleDeExame.domain.role.RoleEnum;
import com.SCX.ControleDeExame.infra.security.TokenService;
import com.SCX.ControleDeExame.repository.AuthRepository;
import com.SCX.ControleDeExame.repository.ExamsFileRepository;
import com.SCX.ControleDeExame.repository.PatientRepository;
import com.SCX.ControleDeExame.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PatientService {

    @Autowired
    PatientRepository patientRepository;
    @Autowired
    AuthRepository authRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    TokenService tokenService;

    @Autowired
    ExamsFileRepository examsFileRepository;

    public void deletePatient(UUID uuid) {

        Patient patient = patientRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("paciente não encontrado"));
        patientRepository.delete(patient);

    }

    public Patient updatePatient(PatientDTO data, UUID uuid) {
        Patient patientUpdate = patientRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("paciente não encontrado"));

        patientUpdate.setTelephone(data.telephone());
        patientUpdate.setDateBirth(data.date_birth());
        return patientRepository.save(patientUpdate);

    }

    public List<GetAllPatientDTO> getAllPatient() {

        return patientRepository.findAll().stream().map(GetAllPatientDTO::new).toList();
    }

    //Metodo para retornar todas as requisições de exame pendentes do paciente
    public List<PatientRequestExamDTO> requestExamPatient (RequestTokenDTO dataT){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Patient patient = patientRepository.findByAuthId_Id(UUID.fromString(id));

        return patientRepository.findRequestExamByPatient(patient.getId());
    }

    //Metodo para retornar todos os exames devolvidos
    public List<ExamsFileDTO> patientResultExam(RequestTokenDTO dataT){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Patient patient = patientRepository.findByAuthId_Id(UUID.fromString(id));

        return examsFileRepository.findByPatient_Id(patient.getId());
    }

    //Metodo para desativar um paciente e anonimizar os dados (testar)
    public void disablePat (RequestTokenDTO dataT){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Optional<Auth> authOPT = authRepository.findById(UUID.fromString(id));
        Auth auth = authOPT.get();
        Patient patient = patientRepository.findByAuthId_Id(auth.getId());
        String encryptedPassword = new BCryptPasswordEncoder().encode("mntvy4-q389");


        auth.setActive(false);
        auth.setName("Nome totalmente anonimo");
        auth.setUsernameKey("Email totalmente anonimo");
        auth.setPassword_key(encryptedPassword);
        authRepository.save(auth);



        patient.setDateBirth(LocalDate.now());
        patient.setTelephone("xxxxxxxxx");
        patient.setCpf("xxxxxxxxx");
        patientRepository.save(patient);

    }

    public List<CliPatDTO> getCliPat (RequestTokenDTO dataT){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Optional<Auth> authOPT = authRepository.findById(UUID.fromString(id));
        Auth auth = authOPT.get();
        Patient patient = patientRepository.findByAuthId_Id(auth.getId());

        return patientRepository.findCliPat(patient.getId());
    }



    public Patient getPatientById(RequestTokenDTO data) {
        return patientRepository.findById(UUID.fromString(data.Token())).orElseThrow(() -> new EntityNotFoundException("paciente não encontrado"));
    }
}
