package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.GetPatientByCPFDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.PatientDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.SecretaryDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.patient.Patient;
import com.SCX.ControleDeExame.domain.role.Role;
import com.SCX.ControleDeExame.domain.secretary.Secretary;
import com.SCX.ControleDeExame.infra.security.TokenService;
import com.SCX.ControleDeExame.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SecretaryService {

    @Autowired
    AuthRepository authRepository;

    @Autowired
    SecretaryRepository secretaryRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    ClinicRepository clinicRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    RoleRepository roleRepository;


    public void deleteSecretary(UUID uuid) {

        Secretary secretary = secretaryRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("registro não encontrado"));
        secretaryRepository.delete(secretary);

    }

    //Metodo para registrar um paciente que não está cadastrado no sistema(testar)
    public void registerPatient(PatientDTO data, RequestTokenDTO dataT) {

        //Criando instâncias do adiministrador que está cadastrando e da clinica que ele está vinculado
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var secretary = secretaryRepository.findByAuthId_Id(UUID.fromString(id));
        Clinic clinic = clinicRepository.findById(secretary.getClinicId().getId()).orElseThrow(() -> new RuntimeException("Clinica não encontrada"));


        Patient newPatient = new Patient();
        Role patient = roleRepository.findByName("Patient");

        String senhaTemp = UUID.randomUUID().toString().substring(0, 8);
        String token = UUID.randomUUID().toString();
        Boolean status = false;
        Timestamp expirationToken = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
        Boolean token_status = true;

        String encryptedPassword = new BCryptPasswordEncoder().encode(senhaTemp);
        Auth newAuth = new Auth(data.email(), data.name(), encryptedPassword, token, status, expirationToken, token_status);

        newAuth.getRoles().add(patient);
        authRepository.save(newAuth);

        try {

            newPatient.setCpf(data.cpf());
            newPatient.setAuthId(newAuth);
            patientRepository.save(newPatient);

            //String tokenE = newAuth.getToken();
            //String url = "http://localhost:5173/firstLogin" + tokenE;

            //emailService.sendEmail(newAuth.getUsernameKey(), "Para ativar sua conta acesse esse link", url);

            clinic.getPatients().add(newPatient);
            clinicRepository.save(clinic);

        } catch (Exception e) {
            clinic.getPatients().remove(newPatient);
            clinicRepository.save(clinic);

            authRepository.delete(newAuth);
            e.printStackTrace();
            throw e;
        }


    }

    //Metodo para ver se o paciente está cadastrado na clínica (testar)
    public boolean patientCli(GetPatientByCPFDTO data, RequestTokenDTO dataT) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var secretary = secretaryRepository.findByAuthId_Id(UUID.fromString(id));
        Clinic clinic = clinicRepository.findById(secretary.getClinicId().getId()).orElseThrow(() -> new EntityNotFoundException("Clinica não encontrada"));

        Patient patient = patientRepository.findByCpf(data.cpf());

        return clinicRepository.existsPatientClinic(clinic.getId(), patient.getId());

    }

    //Metodo para ver ser o paciente está cadastrado no sistema (testar)
    public boolean patientExists(GetPatientByCPFDTO data) {
        return patientRepository.existsByCpf(data.cpf());
    }

    //Metodo para cadastrar um paciente ja cadastrado no sistema em uma nova clinica (testar)
    public void registerPatExistsCli(GetPatientByCPFDTO data, RequestTokenDTO dataT) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var secretary = secretaryRepository.findByAuthId_Id(UUID.fromString(id));

        Clinic clinic = clinicRepository.findById(secretary.getClinicId().getId()).orElseThrow(() -> new EntityNotFoundException("Clinica não encontrada"));

        Patient patient = patientRepository.findByCpf(data.cpf());

        try {
            clinic.getPatients().add(patient);
            clinicRepository.save(clinic);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    public Secretary updateSecretary(SecretaryDTO data, UUID uuid) {
        Secretary secretaryUpdate = secretaryRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("paciente não encontrado"));

        secretaryUpdate.setTelephone(data.telephone());

        return secretaryRepository.save(secretaryUpdate);

    }
}
