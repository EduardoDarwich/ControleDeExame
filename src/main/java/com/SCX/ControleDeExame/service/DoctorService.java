package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.CreateDoctorDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.DoctorVerificDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsDTO.GetByDoctorDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsRequestDTO.ExamsRequestDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    //Metodo para registrar um médico
    public Doctor registerDoctor(CreateDoctorDTO data) {
        //Criando instâncias de usuario
        Auth newAuth = new Auth();
        Optional<Auth> userOPT = authRepository.findAuthByUsernameKey(data.email());
        Role doctor = roleRepository.findByName("Doctor");

        //Adicionando uma role nova ao usuario se ele ja existir
        if (userOPT.isPresent()) {
            Auth user = userOPT.get();
            user.getRoles().add(doctor);
            authRepository.save(user);

            try {
                //Cadastrando dados de médico a esse usuario
                Doctor newDoctor = new Doctor();
                newDoctor.setCrm(data.crm());
                newDoctor.setName(data.name());
                newDoctor.setEmail(data.email());
                newDoctor.setAuthId(user);
                return doctorRepository.save(newDoctor);

            } catch (Exception e) {
                //Removendo role de médico desse usuario caso o cadastro dê errado
                user.getRoles().remove(doctor);
                authRepository.save(user);
                e.printStackTrace();
                throw e;
            }
        }
        //Criando novo usuario
        else {
            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password_key());

            newAuth.setPassword_key(encryptedPassword);
            newAuth.setUsernameKey(data.email());
            newAuth.setActive(true);
            newAuth.setLocked(false);
            newAuth.getRoles().add(doctor);
            authRepository.save(newAuth);

            try {
                //Cadastrando dados de médico ao usuario novo
                Doctor newDoctor = new Doctor();
                newDoctor.setCrm(data.crm());
                newDoctor.setName(data.name());
                newDoctor.setEmail(data.email());
                newDoctor.setAuthId(newAuth);
                return doctorRepository.save(newDoctor);
            } catch (Exception e) {

                authRepository.delete(newAuth);

                e.printStackTrace();
                throw e;
            }
        }



    }

    //Metodo para verificar se o médico ja está cadastrado no sistema
    public Doctor doctorVerific(DoctorVerificDTO data) {
        try {
            return doctorRepository.findByCrm(data.crm());
        } catch (Exception e) {
            throw e;
        }

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
