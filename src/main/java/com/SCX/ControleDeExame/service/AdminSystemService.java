package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.adminSystemDTO.ResponseCliSystDTO;
import com.SCX.ControleDeExame.dataTransferObject.adminSystemDTO.ResponseLabSystDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.RequestCnpjClinicaDTO;
import com.SCX.ControleDeExame.dataTransferObject.adminSystemDTO.ResponsePatSystDTO;
import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.LaboratoryVerificDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.RequestSecretaryEmailDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.domain.role.Role;
import com.SCX.ControleDeExame.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminSystemService {

    @Autowired
    ClinicRepository clinicRepository;

    @Autowired
    LaboratoryRepository laboratoryRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    AuthRepository authRepository;

    @Autowired
    RoleRepository roleRepository;


    //Metodo para desativar uma clinica (testar)
    public void disableClinic(RequestCnpjClinicaDTO data) {
        Clinic clinic = clinicRepository.findByCnpj(data.cnpj());

        clinic.setActive(false);
        clinicRepository.save(clinic);

    }

    //Metodo para ativar uma clinica (testar)
    public void enableClinic(RequestCnpjClinicaDTO data) {
        Clinic clinic = clinicRepository.findByCnpj(data.cnpj());

        clinic.setActive(true);
        clinicRepository.save(clinic);

    }

    //Metodo para listar todas as clinicas do sistema (testar)
    public List<ResponseCliSystDTO> listAllClinics() {

        return clinicRepository.findAllClinicByCnpj();
    }

    //Metodo para listar todos os laboratorios do sistema (testar)
    public List<ResponseLabSystDTO> listAllLaboratory() {

        return laboratoryRepository.findAllLaboratoryByCnpj();
    }

    //Metodo para listar todos os pacientes do sistema (testar)
    public List<ResponsePatSystDTO> listAllPat() {
        return patientRepository.findPatBySyst();
    }

    //Metodo para retornar quantos laboratorios tem cadastrados no sistema (testar)
    public long countLabs() {
        return laboratoryRepository.count();
    }

    //Metodo para retornar quantos pacientes tem cadastrados no sistema (testar)
    public long countPats() {
        return patientRepository.count();
    }

    //Metodo para retornar quantas clinicas tem cadastrados no sistema (testar)
    public long countClinic() {
        return clinicRepository.count();
    }

    //Metodo para desativar um adm de uma clinica (testar)
    public void disableAdmClinic(RequestSecretaryEmailDTO data) {
        Optional<Auth> authOPT = authRepository.findAuthByUsernameKey(data.Email());
        Auth auth = authOPT.get();
        auth.setActive(false);
        authRepository.save(auth);
    }

    //Metodo para ativar um adm de uma clinica (testar)
    public void enableAdmClinic(RequestSecretaryEmailDTO data) {
        Optional<Auth> authOPT = authRepository.findAuthByUsernameKey(data.Email());
        Auth auth = authOPT.get();
        auth.setActive(true);
        authRepository.save(auth);
    }

    //Metodo para criar um usuario do suporte
    public void registerFirstAdmin() {
        Role userSupport = roleRepository.findByName("AdminSystem");
        String senha = "123456789";
        String encryptedPassword = new BCryptPasswordEncoder().encode(senha);

        Auth newAuth = new Auth();
        newAuth.setPassword_key(encryptedPassword);
        newAuth.setUsernameKey("firstadmin@gmail.com");
        newAuth.setName("Goku");
        newAuth.setActive(true);
        newAuth.getRoles().add(userSupport);
        authRepository.save(newAuth);

    }

    //Metodo para desativar um laboratorio (testar)
    public void disableLab(LaboratoryVerificDTO data) {
        Laboratory laboratory = laboratoryRepository.findByCnpj(data.cnpj());
        laboratory.setActive(false);
        laboratoryRepository.save(laboratory);
    }

    //Metodo para ativar um laboratorio (testar)
    public void enableLab(LaboratoryVerificDTO data) {
        Laboratory laboratory = laboratoryRepository.findByCnpj(data.cnpj());
        laboratory.setActive(true);
        laboratoryRepository.save(laboratory);
    }



}
