package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.CreateLabUserAdmDTO;
import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.CreateLabUserDTO;
import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.LaboratoryVerificDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.domain.role.Role;
import com.SCX.ControleDeExame.domain.user_lab.UserLab;
import com.SCX.ControleDeExame.domain.user_lab.UserLabId;
import com.SCX.ControleDeExame.infra.security.TokenService;
import com.SCX.ControleDeExame.repository.AuthRepository;
import com.SCX.ControleDeExame.repository.LaboratoryRepository;
import com.SCX.ControleDeExame.repository.RoleRepository;
import com.SCX.ControleDeExame.repository.UserLabRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LaboratoryService {

    @Autowired
    LaboratoryRepository laboratoryRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    AuthRepository authRepository;

    @Autowired
    UserLabRepository userLabRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    EmailService emailService;


    //Metodo para registrar um usuario administrador para o laboratorio
    public void registerUserAdminLab(CreateLabUserAdmDTO data) {
        Laboratory laboratory = laboratoryRepository.findByCnpj(data.cnpj());
        Role laboratoryAdmin = roleRepository.findByName("LaboratoryAdmin");

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
        newAuth.getRoles().add(laboratoryAdmin);
        authRepository.save(newAuth);

        UserLabId userLabId = new UserLabId(newAuth.getId(), laboratory.getId());

        try {
            UserLab userLab = new UserLab();
            userLab.setId(userLabId);
            userLab.setLaboratoryId(laboratory);
            userLab.setAuthId(newAuth);
            userLab.setEmail(data.email());
            userLabRepository.save(userLab);

            //String tokenE = newAuth.getToken();
            //String url = "http://localhost:5173/firstLogin" + tokenE;

            //emailService.sendEmail(newAuth.getUsernameKey(), "Para ativar sua conta acesse esse link", url);

        } catch (Exception e) {

            authRepository.delete(newAuth);
            e.printStackTrace();
            throw e;
        }

    }

    //Metodo para registrar um usuario comum do laboratório
    public void registerUserLab(CreateLabUserDTO data, RequestTokenDTO dataT){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        UserLab userLab = userLabRepository.findByAuthId_Id(UUID.fromString(id));
        var idLab = userLab.getLaboratoryId().getId();
        Laboratory laboratory = laboratoryRepository.findById(idLab).orElseThrow(() -> new  RuntimeException("Laboratorio nao encontrado"));

        Role laboratoryUser = roleRepository.findByName("LaboratoryUser");

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
        newAuth.getRoles().add(laboratoryUser);
        authRepository.save(newAuth);

        UserLabId userLabId = new UserLabId(newAuth.getId(), laboratory.getId());

        try {
            UserLab newUserLab = new UserLab();
            newUserLab.setId(userLabId);
            newUserLab.setLaboratoryId(laboratory);
            newUserLab.setAuthId(newAuth);
            newUserLab.setEmail(data.email());
            userLabRepository.save(newUserLab);

            //String tokenE = newAuth.getToken();
            //String url = "http://localhost:5173/firstLogin" + tokenE;

            //emailService.sendEmail(newAuth.getUsernameKey(), "Para ativar sua conta acesse esse link", url);

        } catch (Exception e) {
            authRepository.delete(newAuth);
            e.printStackTrace();
            throw e;
        }

    }

    //Metodo para verificar se um laboratorio ja está cadastrado no sistema
    public Laboratory labVerific(LaboratoryVerificDTO data) {
        try {
            return laboratoryRepository.findByCnpj(data.cnpj());
        } catch (Exception e) {
            throw e;
        }
    }

    public void deleteLaboratory(UUID uuid) {

        Laboratory laboratory = laboratoryRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("Registro não encontrado"));
        laboratoryRepository.delete(laboratory);

    }


}