package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.GetAllPatientDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.PatientDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.patient.Patient;
import com.SCX.ControleDeExame.domain.role.Role;
import com.SCX.ControleDeExame.domain.role.RoleEnum;
import com.SCX.ControleDeExame.repository.AuthRepository;
import com.SCX.ControleDeExame.repository.PatientRepository;
import com.SCX.ControleDeExame.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PatientService {

    @Autowired
    PatientRepository patientRepository;
    @Autowired
    AuthRepository authRepository;
    @Autowired
    RoleRepository roleRepository;


    public void registerPatient(PatientDTO data) {

        Role patient = roleRepository.findByName("Patient");
        Auth user = (Auth) authRepository.findByUsernameKey(data.email());

        if (user.getUsernameKey() != null) {

            user.getRoles().add(patient);
            authRepository.save(user);
        } else {

            String senhaTemp = UUID.randomUUID().toString().substring(0, 8);
            String token = UUID.randomUUID().toString();
            Boolean status = false;
            Timestamp expirationToken = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
            Boolean token_status = true;

            String encryptedPassword = new BCryptPasswordEncoder().encode(senhaTemp);
            Auth newAuth = new Auth(data.email(), encryptedPassword, token, status, expirationToken, token_status);

            newAuth.getRoles().add(patient);
            authRepository.save(newAuth);

            try {
                Patient newPatient = new Patient();
                newPatient.setCpf(data.cpf());
                newPatient.setName(data.name());
                newPatient.setEmail(data.email());
                newPatient.setAuthId(newAuth);
                patientRepository.save(newPatient);

            } catch (Exception e) {
                authRepository.delete(newAuth);
                e.printStackTrace();
                throw e;
            }
        }


    }

    public void deletePatient(UUID uuid) {

        Patient patient = patientRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("paciente não encontrado"));
        patientRepository.delete(patient);

    }

    public Patient updatePatient(PatientDTO data, UUID uuid) {
        Patient patientUpdate = patientRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("paciente não encontrado"));

        patientUpdate.setTelephone(data.telephone());
        patientUpdate.setAddress(data.address());
        patientUpdate.setDate_birth(new Date(data.date_birth()));
        return patientRepository.save(patientUpdate);

    }

    public List<GetAllPatientDTO> getAllPatient() {

        return patientRepository.findAll().stream().map(GetAllPatientDTO::new).toList();
    }

    public Patient getPatientByEmail(PatientDTO data) {

        return patientRepository.findByEmail(data.email());
    }

    public Patient getPatientById(RequestTokenDTO data) {
        return patientRepository.findById(UUID.fromString(data.Token())).orElseThrow(() -> new EntityNotFoundException("paciente não encontrado"));
    }
}
