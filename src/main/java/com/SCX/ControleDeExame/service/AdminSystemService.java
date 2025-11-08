package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.RequestCnpjClinicaDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseDocCliDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseLabCliDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.ResponsePatSystDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.RequestSecretaryCpfDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.RequestSecretaryEmailDTO;
import com.SCX.ControleDeExame.domain.admin.Admin;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.domain.secretary.Secretary;
import com.SCX.ControleDeExame.repository.AuthRepository;
import com.SCX.ControleDeExame.repository.ClinicRepository;
import com.SCX.ControleDeExame.repository.LaboratoryRepository;
import com.SCX.ControleDeExame.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
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


    //Metodo para desativar uma clinica (testar)
    public void disableClinic (RequestCnpjClinicaDTO data){
        Clinic clinic = clinicRepository.findByCnpj(data.cnpj());

        clinic.setActive(false);
        clinicRepository.save(clinic);

    }

    //Metodo para ativar uma clinica (testar)
    public void enableClinic (RequestCnpjClinicaDTO data){
        Clinic clinic = clinicRepository.findByCnpj(data.cnpj());

        clinic.setActive(true);
        clinicRepository.save(clinic);

    }

    //Metodo para listar todas as clinicas do sistema (testar)
    public List<ResponseLabCliDTO> listAllClinics (){

        return clinicRepository.findAllClinicByCnpj();
    }

    //Metodo para listar todos os laboratorios do sistema (testar)
    public List<ResponseLabCliDTO> listAllLaboratory (){

        return laboratoryRepository.findAllLaboratoryByCnpj();
    }

    //Metodo para listar todos os pacientes do sistema (testar)
    public List<ResponsePatSystDTO> listAllPat(){
        return patientRepository.findPatBySyst();
    }

    //Metodo para retornar quantos laboratorios tem cadastrados no sistema (testar)
    public long countLabs(){
        return laboratoryRepository.count();
    }

    //Metodo para retornar quantos pacientes tem cadastrados no sistema (testar)
    public long countPats(){
        return patientRepository.count();
    }

    //Metodo para retornar quantas clinicas tem cadastrados no sistema (testar)
    public long countClinic(){
        return clinicRepository.count();
    }

    //Metodo para desativar um adm de uma clinica (testar)
    public void disableAdmClinic (RequestSecretaryEmailDTO data){
        Optional<Auth> authOPT = authRepository.findAuthByUsernameKey(data.Email());
        Auth auth = authOPT.get();
        auth.setActive(false);
        authRepository.save(auth);
    }

    //Metodo para ativar um adm de uma clinica (testar)
    public void enableAdmClinic (RequestSecretaryEmailDTO data){
        Optional<Auth> authOPT = authRepository.findAuthByUsernameKey(data.Email());
        Auth auth = authOPT.get();
        auth.setActive(true);
        authRepository.save(auth);
    }

}
