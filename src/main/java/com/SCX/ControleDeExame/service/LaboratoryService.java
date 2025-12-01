package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsDTO.ExamsDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsRequestDTO.GetExamRequestCodeDTO;
import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.*;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.ExamsFileDTO;
import com.SCX.ControleDeExame.domain.admin.Admin;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.exams.Exams;
import com.SCX.ControleDeExame.domain.examsFile.ExamsFile;
import com.SCX.ControleDeExame.domain.examsRequest.ExamsRequest;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.domain.patient.Patient;
import com.SCX.ControleDeExame.domain.role.Role;
import com.SCX.ControleDeExame.domain.secretary.Secretary;
import com.SCX.ControleDeExame.domain.user_lab.UserLab;
import com.SCX.ControleDeExame.domain.user_lab.UserLabId;
import com.SCX.ControleDeExame.exception.CpfExistException;
import com.SCX.ControleDeExame.exception.EmailExistException;
import com.SCX.ControleDeExame.exception.TelephoneExistException;
import com.SCX.ControleDeExame.infra.security.TokenService;
import com.SCX.ControleDeExame.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
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
    ExamsRepository examsRepository;

    @Autowired
    RequestExamsRepository requestExamsRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    LogService logService;

    @Autowired
    NotificationService notificationService;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    VerifyDataService verifyDataService;

    @Autowired
    ExamsFileRepository examsFileRepository;

    @Autowired
    ExamsFileService examsFileService;


    //Metodo para registrar um usuario administrador para o laboratorio
    public void registerUserAdminLab(CreateLabUserAdmDTO data) {
        Laboratory laboratory = laboratoryRepository.findByCnpj(data.cnpj());
        Role laboratoryAdmin = roleRepository.findByName("LaboratoryAdmin");

        String senhaTemp = UUID.randomUUID().toString().substring(0, 8);
        String token = UUID.randomUUID().toString();
        Timestamp expirationToken = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
        String encryptedPassword = new BCryptPasswordEncoder().encode(senhaTemp);

        if (verifyDataService.verifyEmail(data.email())) {
            throw new EmailExistException();
        }

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




        try {
            UserLabId userLabId = new UserLabId(newAuth.getId(), laboratory.getId());
            UserLab userLab = new UserLab();
            userLab.setId(userLabId);
            userLab.setLaboratoryId(laboratory);
            userLab.setAuthId(newAuth);
            userLab.setEmail(data.email());
            userLabRepository.save(userLab);

            emailService.firtLoginEmail(newAuth);

        } catch (Exception e) {

            authRepository.delete(newAuth);
            e.printStackTrace();
            throw e;
        }

    }

    //Metodo para registrar um usuario comum do laboratório
    public void registerUserLab(CreateLabUserDTO data, RequestTokenDTO dataT) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        UserLab userLab = userLabRepository.findByAuthId_Id(UUID.fromString(id));
        var auth = authRepository.findById(userLab.getAuthId().getId());
        var idLab = userLab.getLaboratoryId().getId();
        Laboratory laboratory = laboratoryRepository.findById(idLab).orElseThrow(() -> new RuntimeException("Laboratorio nao encontrado"));

        Role laboratoryUser = roleRepository.findByName("LaboratoryUser");

        String senhaTemp = UUID.randomUUID().toString().substring(0, 8);
        String token = UUID.randomUUID().toString();
        Timestamp expirationToken = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
        String encryptedPassword = new BCryptPasswordEncoder().encode(senhaTemp);

        if (verifyDataService.verifyEmail(data.email())) {
            throw new EmailExistException();
        }

        Auth newAuth = new Auth();
        newAuth.setUsernameKey(data.email().trim().toLowerCase());
        newAuth.setName(data.name().trim().toLowerCase());
        newAuth.setPassword_key(encryptedPassword);
        newAuth.setActive(false);
        newAuth.setToken(token);
        newAuth.setData_expiration_token(expirationToken);
        newAuth.setToken_status(true);
        newAuth.setLocked(false);
        newAuth.getRoles().add(laboratoryUser);
        authRepository.save(newAuth);

        emailService.firtLoginEmail(newAuth);

        UserLabId userLabId = new UserLabId(newAuth.getId(), laboratory.getId());

        try {
            UserLab newUserLab = new UserLab();
            newUserLab.setId(userLabId);
            newUserLab.setLaboratoryId(laboratory);
            newUserLab.setAuthId(newAuth);
            newUserLab.setEmail(data.email());
            userLabRepository.save(newUserLab);

            logService.logAction(auth.get(), "Registrou um novo usuario para o laboratório");

           emailService.firtLoginEmail(newAuth);

        } catch (Exception e) {
            authRepository.delete(newAuth);
            e.printStackTrace();
            throw e;
        }

    }

    //Metodo para ver todas as clinicas que o laboratorio está cadastrado
    public List<ResponseClinicLabDTO> clinicsLaboratory(RequestTokenDTO dataT) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Auth auth = authRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
        UserLab userLab = userLabRepository.findByAuthId_Id(UUID.fromString(id));
        Optional<Laboratory> laboratoryOPT = laboratoryRepository.findById(userLab.getLaboratoryId().getId());
        Laboratory laboratory = laboratoryOPT.get();

        return laboratoryRepository.findClinicByLaboratory(laboratory.getId());
    }

    //Metodo para retornar se o laboratorio de um usuario está ativo ou não (testar)
    public boolean verificLabActive(RequestTokenDTO dataT) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        UserLab userLab = userLabRepository.findByAuthId_Id(UUID.fromString(id));

        return userLab.getLaboratoryId().isActive();


    }


    public void deleteLaboratory(UUID uuid) {

        Laboratory laboratory = laboratoryRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("Registro não encontrado"));
        laboratoryRepository.delete(laboratory);

    }

    public List<ExamsFileDTO> getExamsLab(RequestTokenDTO dataT){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Auth auth = authRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
        UserLab userLab = userLabRepository.findByAuthId_Id(UUID.fromString(id));
        var idLab = userLab.getLaboratoryId().getId();
        Laboratory laboratory = laboratoryRepository.findById(idLab).orElseThrow(() -> new RuntimeException("Laboratorio nao encontrado"));

        return examsFileRepository.findByLaboratory_Id(laboratory.getId());

    }

    public List<getExamDTO> getExmByRequest (/*RequestTokenDTO dataT,*/ GetExamRequestCodeDTO data) {
        ExamsRequest examsRequest = requestExamsRepository.findByCodVerific(data.code());

        return examsRequest.getExamsFile()
                .stream()
                .map(er -> new getExamDTO(er.getFileName())).toList();

    }

        public void updateExam(RequestTokenDTO dataT, UpdateExamDTO data) throws IOException {
            ExamsFile examsFile = examsFileRepository.findByFileName(data.fileName());
            examsFileService.deleteFile(data.fileName());
            examsFileService.updateFile(data,dataT,examsFile);

            var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
            var id = tokenService.registerUser(idC);
            Auth auth = authRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
            UserLab userLab = userLabRepository.findByAuthId_Id(UUID.fromString(id));
            var idLab = userLab.getLaboratoryId().getId();
            Laboratory laboratory = laboratoryRepository.findById(idLab).orElseThrow(() -> new RuntimeException("Laboratorio nao encontrado"));

            logService.logAction(auth, "Alterou o exame " + data.fileName());


        }




}