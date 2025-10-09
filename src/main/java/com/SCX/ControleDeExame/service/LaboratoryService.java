package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.LaboratoryDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.domain.roleEnum.RoleEnum;
import com.SCX.ControleDeExame.repository.AuthRepository;
import com.SCX.ControleDeExame.repository.LaboratoryRepository;
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
    AuthRepository authRepository;

    public Laboratory registerLaboratory(LaboratoryDTO data) {

        String senhaTemp = UUID.randomUUID().toString().substring(0,8);
        String token = UUID.randomUUID().toString();
        Boolean status = false;
        Timestamp expirationToken = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
        Boolean token_status = true;

        String encryptedPassword = new BCryptPasswordEncoder().encode(senhaTemp);
        Auth newAuth = new Auth(data.cnpj(), encryptedPassword, RoleEnum.LABORATORY, token, status, expirationToken, token_status);
        authRepository.save(newAuth);


        try {
            Laboratory newLaboratory = new Laboratory();
            newLaboratory.setName(data.name());
            newLaboratory.setCnpj(data.cnpj());
            newLaboratory.setEmail(data.email());
            newLaboratory.setAddress(data.address());
            newLaboratory.setTelephone(data.telephone());
            newLaboratory.setAuthId(newAuth);
            return laboratoryRepository.save(newLaboratory);

        } catch (Exception e) {
            authRepository.delete(newAuth);
            e.printStackTrace();
            throw e;
        }

    }
    public void deleteLaboratory(UUID uuid) {

        Laboratory laboratory = laboratoryRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("Registro não encontrado"));
        laboratoryRepository.delete(laboratory);

    }


}
