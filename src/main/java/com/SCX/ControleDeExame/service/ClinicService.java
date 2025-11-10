package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.adminDTO.CreateFirstAdmDTO;
import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.CreateClinicDTO;
import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.CreateLaboratoryDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.RequestSecretaryEmailDTO;
import com.SCX.ControleDeExame.domain.address.Address;
import com.SCX.ControleDeExame.domain.admin.Admin;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
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

    @Autowired
    EmailService emailService;

    @Autowired
    LogService logService;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    SecretaryRepository secretaryRepository;

    //Metodo para registrar uma clinica
    public void registerClinic(CreateClinicDTO data) {
        Address address = new Address();
        address.setCep(data.cep());
        address.setLogradouro(data.logradouro());
        address.setComplemento(data.complemento());
        address.setBairro(data.bairro());
        address.setUf(data.uf());
        addressRepository.save(address);

        Clinic newClinic = new Clinic();
        newClinic.setName(data.name());
        newClinic.setCnpj(data.cnpj());
        newClinic.setTelephone(data.telephone());
        newClinic.setAddress(address);
        newClinic.setActive(true);
        clinicRepository.save(newClinic);


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

        } else {

            try {


                newAdmin.setAuthId(newAuth);
                newAdmin.setClinicId(clinic);
                adminRepository.save(newAdmin);

                clinic.getAdmins().add(newAdmin);
                clinicRepository.save(clinic);

                emailService.firtLoginEmail(newAuth);

            } catch (Exception e) {
                authRepository.delete(newAuth);
                clinic.getAdmins().remove(newAdmin);
            }

        }


    }

    //Metodo para registrar um laboratorio
    public void registerNewLaboratory(CreateLaboratoryDTO data, RequestTokenDTO dataT) {

        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var admin = adminRepository.findByAuthId_Id(UUID.fromString(id));
        var auth = authRepository.findById(admin.getAuthId().getId());
        var clinic = clinicRepository.findById(admin.getClinicId().getId()).orElseThrow(() -> new EntityNotFoundException("Clinica não encontrada"));

        Address address = new Address();
        address.setCep(data.cep());
        address.setLogradouro(data.logradouro());
        address.setComplemento(data.complemento());
        address.setBairro(data.bairro());
        address.setUf(data.uf());
        addressRepository.save(address);

        Laboratory newLaboratory = new Laboratory();
        newLaboratory.setName(data.name());
        newLaboratory.setCnpj(data.cnpj());
        newLaboratory.setTelephone(data.telephone());
        newLaboratory.setAddress(address);
        newLaboratory.setActive(true);
        laboratoryRepository.save(newLaboratory);

        clinic.getLaboratories().add(newLaboratory);
        clinicRepository.save(clinic);

        logService.logAction(auth.get(), "Registrou um novo laboratório na clinica");

    }

    //Metodo para retornar se a clinica de um usuario está ativa ou não (testar)
    public boolean verificCliActive (RequestTokenDTO dataT){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        if (adminRepository.findByAuthId_Id(UUID.fromString(id)) != null){
            Admin admin = adminRepository.findByAuthId_Id(UUID.fromString(id));
            return admin.getClinicId().isActive();
        } else {
            Secretary secretary = secretaryRepository.findByAuthId_Id(UUID.fromString(id));
            return secretary.getClinicId().isActive();
        }

    }



}