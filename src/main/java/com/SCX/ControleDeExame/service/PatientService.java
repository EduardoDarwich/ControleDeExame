package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.GetAllPatientDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.GetPatientByCPFDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.PatientDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.patient.Patient;
import com.SCX.ControleDeExame.domain.role.Role;
import com.SCX.ControleDeExame.domain.role.RoleEnum;
import com.SCX.ControleDeExame.infra.security.TokenService;
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
    @Autowired
    TokenService tokenService;





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


    public Patient getPatientById(RequestTokenDTO data) {
        return patientRepository.findById(UUID.fromString(data.Token())).orElseThrow(() -> new EntityNotFoundException("paciente não encontrado"));
    }
}
