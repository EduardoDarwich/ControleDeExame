package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.CreateDoctorDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.CreateUserDocExistsDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.DoctorVerificDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsDTO.GetByDoctorDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsRequestDTO.ExamsRequestDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.exams.Exams;
import com.SCX.ControleDeExame.domain.examsRequest.ExamsRequest;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.domain.patient.Patient;
import com.SCX.ControleDeExame.domain.role.Role;
import com.SCX.ControleDeExame.infra.security.TokenService;
import com.SCX.ControleDeExame.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

//Classe contendo a logica da entidade do médico
@Service
public class DoctorService {
    //Criando instâncias das classes utilizadas
    @Autowired
    AuthRepository authRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    RequestExamsRepository requestExamsRepository;

    @Autowired
    ExamsRepository examsRepository;

    @Autowired
    LaboratoryRepository laboratoryRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    ClinicRepository clinicRepository;

    //Metodo para registrar um médico
    public void registerDoctor(CreateDoctorDTO data, RequestTokenDTO dataT) {

        //Criando instâncias do adiministrador que está cadastrando e da clinica que ele está vinculado
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var admin = adminRepository.findByAuthId_Id(UUID.fromString(id));
        Clinic clinic = clinicRepository.findById(admin.getClinicId().getId()).orElseThrow(() -> new RuntimeException("Clinica não encontrada"));

        //Criando instâncias de usuario e médico
        Auth newAuth = new Auth();
        Doctor newDoctor = new Doctor();
        Role doctor = roleRepository.findByName("Doctor");

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
        newAuth.getRoles().add(doctor);
        authRepository.save(newAuth);

        try {
            //Cadastrando dados de médico ao usuario novo
            newDoctor.setCrm(data.crm());
            newDoctor.setAuthId(newAuth);
            doctorRepository.save(newDoctor);

            //Adicionadno o médico criado a clinica na qual ele está sendo cadastrado
            clinic.getDoctors().add(newDoctor);
            clinicRepository.save(clinic);
        } catch (Exception e) {
            clinic.getDoctors().remove(newDoctor);
            clinicRepository.save(clinic);

            authRepository.delete(newAuth);
            e.printStackTrace();
            throw e;
        }

    }
    //Metodo para cadastrar um medico que ja existe no sistema em uma clinica
    public void registerDocUserExists(CreateUserDocExistsDTO data, RequestTokenDTO dataT){

        //Criando instâncias do adiministrador que está cadastrando e da clinica que ele está vinculado
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var admin = adminRepository.findByAuthId_Id(UUID.fromString(id));
        Clinic clinic = clinicRepository.findById(admin.getClinicId().getId()).orElseThrow(() -> new RuntimeException("Clinica não encontrada"));

        Doctor docUser = doctorRepository.findByCrm(data.crm());

        try {
            clinic.getDoctors().add(docUser);
            clinicRepository.save(clinic);
        } catch (Exception e){

            clinic.getDoctors().remove(docUser);
            clinicRepository.save(clinic);
            e.printStackTrace();
            throw e;
        }

    }

    //Metodo para verificar se o médico ja está cadastrado no sistema
    public Doctor doctorVerific(DoctorVerificDTO data) {
        Doctor doctor = doctorRepository.findByCrm(data.crm());
        return doctor;
    }
    //Metodo para verificar se o usuario está cadastrado na clinica
    public Boolean verificDocCli(DoctorVerificDTO data, RequestTokenDTO dataT){

        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var admin = adminRepository.findByAuthId_Id(UUID.fromString(id));
        Clinic clinic = clinicRepository.findById(admin.getClinicId().getId()).orElseThrow(() -> new RuntimeException("Clinica não encontrada"));
        Doctor doctor = doctorRepository.findByCrm(data.crm());

        Boolean exists = clinicRepository.existsDoctorClinic(clinic.getId(), doctor.getId());
        return exists;
    }

    public void deleteDoctor(UUID uuid) {
        Doctor doctor = doctorRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("paciente não encontrado"));
        doctorRepository.delete(doctor);

    }

    public Doctor updateDoctor(CreateDoctorDTO data, UUID uuid) {
        Doctor doctorUpdate = doctorRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("paciente não encontrado"));
        doctorUpdate.setTelephone(data.telephone());
        return doctorRepository.save(doctorUpdate);

    }

    public List<GetByDoctorDTO> getExamsByDoctor(RequestTokenDTO data) {
        var idC = data.toString().replace("RequestTokenDTO[Token=", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Doctor doctorId = doctorRepository.findByAuthId_Id(UUID.fromString(id));
        return examsRepository.findAllByDoctorId(doctorId.getId());
    }

    public void requestExams(ExamsRequestDTO data, RequestTokenDTO dataT) {
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dataToString = now.format(formatter);
            var idC = dataT.toString().replace("RequestTokenDTO[Token=", "").replace("]", "");
            var id = tokenService.registerUser(idC);
            Doctor doctor = doctorRepository.findByAuthId_Id(UUID.fromString(id));
            ExamsRequest newExamRequest = new ExamsRequest();
            newExamRequest.setDoctorId(doctor);
            newExamRequest.setExam_type(data.exam_type());
            newExamRequest.setSample_type(data.sample_type());
            newExamRequest.setComplement(data.complement());
            newExamRequest.setRequestDate(dataToString);
            requestExamsRepository.save(newExamRequest);
            String cnpj = data.cnpj();
            String cpf = data.cpf();
            Laboratory laboratory = laboratoryRepository.findByCnpj(cnpj);
            Patient patient = patientRepository.findByCpf(cpf);

            Exams newExam = new Exams();
            newExam.setStatus("Pendente");
            newExam.setDoctor(doctor);
            newExam.setRequestId(newExamRequest);
            newExam.setLaboratoryId(laboratory);
            newExam.setPatientId(patient);
            examsRepository.save(newExam);


        } catch (Exception e) {
            e.printStackTrace();
            throw e;

        }
    }

    //Metodo para procurar exames por status
    public void getByExamsStatus() {
    }

    //Metodo para buscar exames associados a um paciente
    public void getByPatientName() {
    }
}
