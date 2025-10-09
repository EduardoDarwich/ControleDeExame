package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.DoctorDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsDTO.GetByDoctorDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsRequestDTO.ExamsRequestDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.exams.Exams;
import com.SCX.ControleDeExame.domain.examsRequest.ExamsRequest;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.domain.patient.Patient;
import com.SCX.ControleDeExame.domain.roleEnum.RoleEnum;
import com.SCX.ControleDeExame.infra.security.TokenService;
import com.SCX.ControleDeExame.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class DoctorService {

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

    public Doctor registerDoctor(DoctorDTO data) {

        String senhaTemp = UUID.randomUUID().toString().substring(0, 8);
        String token = UUID.randomUUID().toString();
        Boolean status = false;
        Timestamp expirationToken = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
        Boolean token_status = true;

        String encryptedPassword = new BCryptPasswordEncoder().encode(senhaTemp);
        Auth newAuth = new Auth(data.crm(), encryptedPassword, RoleEnum.DOCTOR, token, status, expirationToken, token_status);
        authRepository.save(newAuth);


        try {
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

    public void deleteDoctor(UUID uuid) {

        Doctor doctor = doctorRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("paciente não encontrado"));
        doctorRepository.delete(doctor);

    }

    public Doctor updateDoctor(DoctorDTO data, UUID uuid) {
        Doctor doctorUpdate = doctorRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("paciente não encontrado"));

        doctorUpdate.setTelephone(data.telephone());
        return doctorRepository.save(doctorUpdate);

    }


    public List<GetByDoctorDTO> getExamsByDoctor(RequestTokenDTO data) {

        var idC = data.toString().replace("RequestTokenDTO[Token=", "" ).replace("]", "");
        var id = tokenService.registerUser(idC);
        Doctor doctorId = doctorRepository.findByAuthId_Id(UUID.fromString(id));
        return examsRepository.findAllByDoctorId(doctorId.getId());
    }

    public void requestExams(ExamsRequestDTO data, RequestTokenDTO dataT) {
        try {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dataToString = now.format(formatter);
            var idC = dataT.toString().replace("RequestTokenDTO[Token=", "" ).replace("]", "");
            var id = tokenService.registerUser(idC);
            System.out.println(id);

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

    public void getByExamsStatus() {
    }

    public void getByPatientName() {
    }
}
