package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.adminDTO.CreateFirstAdmDTO;
import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.CreateClinicAdminDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.CreateClinicDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.RequestCnpjClinica;
import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.CreateLaboratoryDTO;
import com.SCX.ControleDeExame.domain.admin.Admin;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.domain.role.Role;
import com.SCX.ControleDeExame.domain.user_lab.UserLab;
import com.SCX.ControleDeExame.infra.security.TokenService;
import com.SCX.ControleDeExame.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClinicService {

    @Autowired
    ClinicRepository clinicRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    AuthRepository authRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    LaboratoryRepository laboratoryRepository;

    //Metodo para registrar uma clinica
    public Clinic regiterClinic(CreateClinicDTO data) {
        Clinic newClinic = new Clinic();
        newClinic.setAddress(data.address());
        newClinic.setName(data.name());
        newClinic.setCnpj(data.cnpj());
        newClinic.setTelephone(data.telephone());

        return clinicRepository.save(newClinic);
    }

    //Metodo para criar o primeiro adm da clinica
    public void createFirstAdmin(CreateFirstAdmDTO data) {
        Clinic clinic = clinicRepository.findByCnpj(data.cnpj());
        Admin newAdmin = new Admin();
        Role admin = roleRepository.findByName("Admin");

        String senhaTemp = UUID.randomUUID().toString().substring(0, 8);
        String token = UUID.randomUUID().toString();
        Timestamp expirationToken = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
        String encryptedPassword = new BCryptPasswordEncoder().encode(senhaTemp);

        Auth newAuth = new Auth();
        newAuth.setUsernameKey(data.email());
        newAuth.setName(data.name());
        newAuth.setPassword_key(encryptedPassword);
        newAuth.setActive(false);
        newAuth.setToken(token);
        newAuth.setData_expiration_token(expirationToken);
        newAuth.setToken_status(true);
        newAuth.setLocked(false);
        newAuth.getRoles().add(admin);
        authRepository.save(newAuth);

        if (clinic == null) {

            authRepository.delete(newAuth);
            clinic.getUsers().remove(newAuth);


        } else {

            try {
                clinic.getUsers().add(newAuth);
                clinicRepository.save(clinic);

                newAdmin.setAuthId(newAuth);
                newAdmin.setClinicId(clinic);
                adminRepository.save(newAdmin);
            } catch (Exception e) {
                authRepository.delete(newAuth);
                clinic.getUsers().remove(newAuth);
            }

        }


    }

    //Metodo para registrar um laboratorio
    public void registerNewLaboratory(CreateLaboratoryDTO data, RequestTokenDTO dataT) {

        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var admin = adminRepository.findByAuthId_Id(UUID.fromString(id));
        var clinic = clinicRepository.findById(admin.getClinicId().getId()).orElseThrow(() -> new EntityNotFoundException("Clinica não encontrada"));


        Laboratory newLaboratory = new Laboratory();
        newLaboratory.setName(data.name());
        newLaboratory.setCnpj(data.cnpj());
        newLaboratory.setAddress(data.address());
        newLaboratory.setTelephone(data.telephone());
        laboratoryRepository.save(newLaboratory);

        clinic.getLaboratories().add(newLaboratory);
        clinicRepository.save(clinic);

    }


}
