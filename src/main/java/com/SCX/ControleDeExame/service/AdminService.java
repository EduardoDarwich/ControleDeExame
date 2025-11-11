package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.adminDTO.CreateAdminDTO;
import com.SCX.ControleDeExame.dataTransferObject.adminDTO.ResponseAdminClinicDTO;
import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseDocCliDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseLabCliDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseSecretaryCliDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.DoctorVerificDTO;
import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.LaboratoryVerificDTO;
import com.SCX.ControleDeExame.dataTransferObject.logDTO.LogDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.RequestSecretaryCpfDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.RequestSecretaryEmailDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.SecretaryDTO;
import com.SCX.ControleDeExame.domain.admin.Admin;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.domain.role.Role;
import com.SCX.ControleDeExame.domain.secretary.Secretary;
import com.SCX.ControleDeExame.domain.user_lab.UserLab;
import com.SCX.ControleDeExame.infra.security.TokenService;
import com.SCX.ControleDeExame.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    LogService logService;

    @Autowired
    DoctorRepository doctorRepository;


    //Metodo para criar um usuário de adiministrador
    public Admin registerAdm(CreateAdminDTO data, RequestTokenDTO dataT) {
        //Criando instancias de usuario
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var admin = adminRepository.findByAuthId_Id(UUID.fromString(id));
        var auth = authRepository.findById(admin.getAuthId().getId());
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



        try {

            //Cadastrando dados de admin ao usuario novo;
            Admin newAdmin = new Admin();
            newAdmin.setTelephone(data.telephone());
            newAdmin.setCpf(data.cpf());
            newAdmin.setAuthId(newAuth);
            newAdmin.setClinicId(clinic);

            emailService.firtLoginEmail(newAuth);

            clinic.getAdmins().add(newAdmin);
            clinicRepository.save(clinic);

            logService.logAction(auth.get(), "Registrou um novo administrador na clinica");

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

    //Metodo para consultar os medicos de uma clinica pelo Id do administrador logado
    public List<ResponseDocCliDTO> docCli (RequestTokenDTO dataT){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var admin = adminRepository.findByAuthId_Id(UUID.fromString(id));
        Clinic clinic = clinicRepository.findById(admin.getClinicId().getId()).orElseThrow(() -> new RuntimeException("Clinica não encontrada"));

        return clinicRepository.findDocByClinic(clinic.getId());

    }

    //Metodo para criar uma secretaria nova
    public void registerSecretary(SecretaryDTO data, RequestTokenDTO dataT) {


        //Criando instâncias do adiministrador que está cadastrando e da clinica que ele está vinculado
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var admin = adminRepository.findByAuthId_Id(UUID.fromString(id));
        var auth = authRepository.findById(admin.getAuthId().getId());
        Clinic clinic = clinicRepository.findById(admin.getClinicId().getId()).orElseThrow(() -> new RuntimeException("Clinica não encontrada"));

        //Criando instâncias de usuario e médico
        Auth newAuth = new Auth();
        Secretary newSecretary = new Secretary();
        Role secretary = roleRepository.findByName("Secretary");

        //Criando senha temporaria e token para primeiro login
        String senhaTemp = UUID.randomUUID().toString().substring(0, 8);
        String token = UUID.randomUUID().toString();
        Timestamp expirationToken = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
        String encryptedPassword = new BCryptPasswordEncoder().encode(senhaTemp);

        newAuth.setPassword_key(encryptedPassword);
        newAuth.setUsernameKey(data.email());
        newAuth.setName(data.name());
        newAuth.setActive(false);
        newAuth.setToken(token);
        newAuth.setData_expiration_token(expirationToken);
        newAuth.setToken_status(true);
        newAuth.setLocked(false);
        newAuth.getRoles().add(secretary);
        authRepository.save(newAuth);

        emailService.firtLoginEmail(newAuth);

        try {
            newSecretary.setCpf(data.cpf());
            newSecretary.setAuthId(newAuth);
            newSecretary.setClinicId(clinic);
            newSecretary.setTelephone(data.telephone());
            secretaryRepository.save(newSecretary);
            logService.logAction(auth.get(), "Registrou um novo usuario da secretaria");

        } catch (Exception e) {
            authRepository.delete(newAuth);
            e.printStackTrace();
            throw e;
        }

    }

    //Metodo para ver ser a secretaria está cadastrada no sistema
    public boolean secretaryExists (RequestSecretaryCpfDTO data) {
        return  secretaryRepository.existsByCpf(data.cpf());
    }

    //Metodo para verificar se um laboratório está cadastrado na clinica
    public boolean verificLabCLi (RequestTokenDTO dataT, LaboratoryVerificDTO data ){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Admin admin = adminRepository.findByAuthId_Id(UUID.fromString(id));
        Clinic clinic = clinicRepository.findById(admin.getClinicId().getId()).orElseThrow(() -> new RuntimeException("Clinica não encontrada"));

        Laboratory laboratory = laboratoryRepository.findByCnpj(data.cnpj());

        return clinicRepository.existsLaboratoryClinic(clinic.getId(),laboratory.getId());
    }

    //Metodo para verificar se um laboratorio ja está cadastrado no sistema
    public boolean labVerific(LaboratoryVerificDTO data) {
        try {
            return laboratoryRepository.existsByCnpj(data.cnpj());
        } catch (Exception e) {
            throw e;
        }
    }

    //Metodo para cadastrar um laboratorio que ja existe no sistema em uma clinica
    public void registerLabExists(LaboratoryVerificDTO data, RequestTokenDTO dataT){

        //Criando instâncias do adiministrador que está cadastrando e da clinica que ele está vinculado
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var admin = adminRepository.findByAuthId_Id(UUID.fromString(id));
        var auth = authRepository.findById(admin.getAuthId().getId());
        Clinic clinic = clinicRepository.findById(admin.getClinicId().getId()).orElseThrow(() -> new RuntimeException("Clinica não encontrada"));

        Laboratory laboratory = laboratoryRepository.findByCnpj(data.cnpj());

        try {
            clinic.getLaboratories().add(laboratory);
            clinicRepository.save(clinic);
            logService.logAction(auth.get(), "Registrou um novo laboratório na clinica");
        } catch (Exception e){

            e.printStackTrace();
            throw e;
        }

    }

    //Metodo para devolver os laboratorios de uma clinica
    public List<ResponseLabCliDTO> labCli (RequestTokenDTO dataT){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var admin = adminRepository.findByAuthId_Id(UUID.fromString(id));
        Clinic clinic = clinicRepository.findById(admin.getClinicId().getId()).orElseThrow(() -> new RuntimeException("Clinica não encontrada"));

        return clinicRepository.findLabByClinic(clinic.getId());
    }

    //Metodo para listar todas as secretarias da clinica(testar)
    public List<ResponseSecretaryCliDTO> responseSecretaryCli (RequestTokenDTO dataT){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var admin = adminRepository.findByAuthId_Id(UUID.fromString(id));
        Clinic clinic = clinicRepository.findById(admin.getClinicId().getId()).orElseThrow(() -> new RuntimeException("Clinica não encontrada"));

        return clinicRepository.findSecretaryByClinic(clinic.getId());

    }

    //Metodo para desativar um usuario da secretaria(testar)
    public void disableSecretary(RequestSecretaryEmailDTO data) {
        Optional<Auth> authOPT = authRepository.findAuthByUsernameKey(data.Email());
        Auth auth = authOPT.get();
        auth.setActive(false);
        authRepository.save(auth);
    }

    //Metodo para ativar um usuário da secretaria(testar)
    public void enableSecretary(RequestSecretaryEmailDTO data) {
        Optional<Auth> authOPT = authRepository.findAuthByUsernameKey(data.Email());
        Auth auth = authOPT.get();
        auth.setActive(true);
        authRepository.save(auth);
    }

    //Metodo para desativar um laboratorio(testar)
    public void disableLaboratory(RequestTokenDTO dataT, LaboratoryVerificDTO data) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var admin = adminRepository.findByAuthId_Id(UUID.fromString(id));
        Clinic clinic = clinicRepository.findById(admin.getClinicId().getId()).orElseThrow(() -> new RuntimeException("Clinica não encontrada"));

        String cnpj = data.cnpj();
        Laboratory laboratory = laboratoryRepository.findByCnpj(cnpj);

        clinic.getLaboratories().remove(laboratory);
        clinicRepository.save(clinic);

    }

    //Metodo para ativar um laboratorio(testar)
    public void enableLaboratory(RequestTokenDTO dataT, LaboratoryVerificDTO data) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var admin = adminRepository.findByAuthId_Id(UUID.fromString(id));
        Clinic clinic = clinicRepository.findById(admin.getClinicId().getId()).orElseThrow(() -> new RuntimeException("Clinica não encontrada"));

        String cnpj = data.cnpj();
        Laboratory laboratory = laboratoryRepository.findByCnpj(cnpj);

        clinic.getLaboratories().add(laboratory);
        clinicRepository.save(clinic);

    }

    //Metodo para desativar um usuario do laboratorio(testar)
    public void disableLabUser (RequestSecretaryEmailDTO data){
       Auth auth = (Auth) authRepository.findByUsernameKey(data.Email());
       auth.setActive(false);
       authRepository.save(auth);

    }

    //Metodo para ativar um usuario do laboratorio(testar)
    public void enableLabUser (RequestSecretaryEmailDTO data){
        Auth auth = (Auth) authRepository.findByUsernameKey(data.Email());
        auth.setActive(true);
        authRepository.save(auth);
    }


    //Metodo para desvincular um laboratorio (testar)
    public void disableDocCli(RequestTokenDTO dataT, DoctorVerificDTO data) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var admin = adminRepository.findByAuthId_Id(UUID.fromString(id));
        Clinic clinic = clinicRepository.findById(admin.getClinicId().getId()).orElseThrow(() -> new RuntimeException("Clinica não encontrada"));

        String crm = data.crm();
        Doctor doctor = doctorRepository.findByCrm(crm);

        clinic.getDoctors().remove(doctor);
        clinicRepository.save(clinic);

    }
    



}