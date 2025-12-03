package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.repository.AdminRepository;
import com.SCX.ControleDeExame.repository.AuthRepository;
import com.SCX.ControleDeExame.repository.PatientRepository;
import com.SCX.ControleDeExame.repository.SecretaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerifyDataService {
    @Autowired
    AuthRepository authRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    SecretaryRepository secretaryRepository;

    @Autowired
    PatientRepository patientRepository;

    public boolean verifyCpf(String cpf) {


        if (patientRepository.existsByCpf(cpf) && cpf != null ) {

            return true;
        } else if (secretaryRepository.existsByCpf(cpf) && cpf != null) {

            return true;
        } else if (adminRepository.existsByCpf(cpf) && cpf != null) {
            return true;
        }

        return false;
    }

    public boolean verifyTelephone (String telephone){
        if(patientRepository.existsByTelephone(telephone) && telephone != null){
            return true;
        } else if (secretaryRepository.existsByTelephone(telephone) && telephone != null){
            return true;
        } else if (adminRepository.existsByTelephone(telephone) && telephone != null) {
            return true;
        }

        return false;
    }

    public boolean verifyEmail (String email){
        if (authRepository.existsByUsernameKey(email)){
            return true;
        }
        return false;
    }






}
