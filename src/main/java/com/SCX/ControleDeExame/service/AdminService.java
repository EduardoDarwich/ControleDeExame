package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.adminDTO.CreateAdminDTO;
import com.SCX.ControleDeExame.dataTransferObject.adminDTO.ResponseAdminClinicDTO;
import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.logDTO.LogDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.SecretaryDTO;
import com.SCX.ControleDeExame.domain.admin.Admin;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
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
import java.util.List;
import java.util.UUID;

//@Service indica pro spring que essa class é uma Service
@Service
public class AdminService {

    //@Autowired cria uma instância de uma certa class
    @Autowired
    AdminRepository adminRepository;

    @Autowired
    SecretaryRepository secretaryRepository;

    @Autowired
    AuthRepository authRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    LaboratoryRepository laboratoryRepository;

    @Autowired
    LogRepository logRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    ClinicRepository clinicRepository;


    //Metodo para criar um usuário de adiministrador
    public Admin registerAdm(CreateAdminDTO data, RequestTokenDTO dataT) {
        //Criando instancias de usuario
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var admin = adminRepository.findByAuthId_Id(UUID.fromString(id));
        Clinic clinic = clinicRepository.findById(admin.getClinicId().getId()).orElseThrow(() -> new RuntimeException("Clinica não encontrada"));

        Role adminRole = roleRepository.findByName("Admin");

        String senhaTemp = UUID.randomUUID().toString().substring(0, 8);
        String token = UUID.randomUUID().toString();
        Timestamp expirationToken = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
        String encryptedPassword = new BCryptPasswordEncoder().encode(senhaTemp);

        Auth newAuth = new Auth();
        newAuth.setName(data.name());
        newAuth.setUsernameKey(data.email());
        newAuth.setPassword_key(encryptedPassword);
        newAuth.setActive(false);
        newAuth.setToken(token);
        newAuth.setData_expiration_token(expirationToken);
        newAuth.setToken_status(true);
        newAuth.setLocked(false);
        newAuth.getRoles().add(adminRole);
        authRepository.save(newAuth);

        clinic.getUsers().add(newAuth);
        clinicRepository.save(clinic);

        try {

            //Cadastrando dados de admin ao usuario novo;
            Admin newAdmin = new Admin();
            newAdmin.setTelephone(data.telephone());
            newAdmin.setCpf(data.cpf());
            newAdmin.setAuthId(newAuth);
            newAdmin.setClinicId(clinic);

            //String tokenE = newAuth.getToken();
            // String url = "http://localhost:5173/firstLogin" + tokenE;

            //emailService.sendEmail(newAuth.getUsernameKey(), "Para ativar sua conta acesse esse link", url);

            return adminRepository.save(newAdmin);

        } catch (Exception e) {
            authRepository.delete(newAuth);
            e.printStackTrace();
            throw e;
        }


    }

    //Metodo para devolver a clinica que o administrador está
    public ResponseAdminClinicDTO clinicAdm(RequestTokenDTO dataT) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Auth auth = authRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
        Admin admin = adminRepository.findByAuthId_Id(auth.getId());
        Clinic clinic = clinicRepository.findById(admin.getClinicId().getId()).orElseThrow(() -> new EntityNotFoundException("Clinica não encontrada"));
        return new ResponseAdminClinicDTO(clinic.getName());
    }

    //Metodo para cadastrar um usuario ja existente como adiministrador
    /*public Admin registerAdmInUser(CreateAdminDTO data, RequestTokenDTO dataT) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var admin = adminRepository.findByAuthId_Id(UUID.fromString(id));

        Role adminRole = roleRepository.findByName("Admin");
        Optional<Auth> userOPT = authRepository.findAuthByUsernameKey(data.email());

        Auth user = userOPT.get();
        user.getRoles().add(adminRole);
        authRepository.save(user);

        try {
            //Cadastrando dados de admin a esse usuario;
            Admin newAdmin = new Admin();
            newAdmin.setName(data.name());
            newAdmin.setEmail(data.email());
            newAdmin.setTelephone(data.telephone());
            newAdmin.setCpf(data.cpf());
            newAdmin.setAuthId(user);
            return adminRepository.save(newAdmin);

        } catch (Exception e) {
            //Removendo a role dele caso tenha problema no cadastro como admin
            user.getRoles().remove(admin);
            authRepository.save(user);
            e.printStackTrace();
            throw e;

        }


    }*/


    public Secretary registerSecretary(SecretaryDTO data) {

        String senhaTemp = UUID.randomUUID().toString().substring(0, 8);
        String token = UUID.randomUUID().toString();
        Boolean status = false;
        Timestamp expirationToken = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
        Boolean token_status = true;
        String encryptedPassword = new BCryptPasswordEncoder().encode(senhaTemp);
        Auth newAuth = new Auth(data.email(), data.name(), encryptedPassword, token, status, expirationToken, token_status);
        authRepository.save(newAuth);


        try {
            Secretary newSecretary = new Secretary();
            newSecretary.setCpf(data.cpf());
            newSecretary.setAuthId(newAuth);
            return secretaryRepository.save(newSecretary);

        } catch (Exception e) {
            authRepository.delete(newAuth);
            e.printStackTrace();
            throw e;
        }

    }


/*
    public void disableLaboratory(GetLaboratoryCNPJDTO data) {
        String cnpj = data.cnpj();
        Laboratory laboratory = laboratoryRepository.findByCnpj(cnpj);
        Auth auth = authRepository.findById(laboratory.getAuthId().getId()).orElseThrow();
        auth.setActive(false);

    }

    public void enableLaboratory(GetLaboratoryCNPJDTO data) {
        String cnpj = data.cnpj();
        Laboratory laboratory = laboratoryRepository.findByCnpj(cnpj);
        Auth auth = authRepository.findById(laboratory.getAuthId().getId()).orElseThrow();
        auth.setActive(true);

    }

    public void enableSecretary(RequestSecretaryEmailDTO data) {
        String email = data.Email();
        Secretary secretary = secretaryRepository.findByEmail(email);
        Auth auth = authRepository.findById(secretary.getAuthId().getId()).orElseThrow();
        auth.setActive(true);

    }*/

    public List<LogDTO> getAllLog() {

        return logRepository.findAll().stream().map(LogDTO::new).toList();
    }


}