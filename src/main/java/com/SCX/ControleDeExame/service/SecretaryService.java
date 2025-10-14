package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.SecretaryDTO;
import com.SCX.ControleDeExame.domain.secretary.Secretary;
import com.SCX.ControleDeExame.repository.AuthRepository;
import com.SCX.ControleDeExame.repository.SecretaryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SecretaryService {

    @Autowired
    AuthRepository authRepository;

    @Autowired
    SecretaryRepository secretaryRepository;



    public void deleteSecretary(UUID uuid) {

        Secretary secretary = secretaryRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("registro não encontrado"));
        secretaryRepository.delete(secretary);

    }

    public Secretary updateSecretary (SecretaryDTO data, UUID uuid){
        Secretary secretaryUpdate = secretaryRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("paciente não encontrado"));

        secretaryUpdate.setTelephone(data.telephone());

        return secretaryRepository.save(secretaryUpdate);

    }
}
