package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.appointmentDTO.RegisterAppointmentDTO;
import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseDocCliConsultDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponsePatCliDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.GetPatientByCPFDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.PatientDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.ResponseSecretaryClinicDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.SecretaryDTO;
import com.SCX.ControleDeExame.domain.appointment.Appointment;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.patient.Patient;
import com.SCX.ControleDeExame.domain.role.Role;
import com.SCX.ControleDeExame.domain.secretary.Secretary;
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

@Service
public class SecretaryService {

    @Autowired
    AuthRepository authRepository;

    @Autowired
    SecretaryRepository secretaryRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    ClinicRepository clinicRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    DoctorRepository doctorRepository;


    public void deleteSecretary(UUID uuid) {

        Secretary secretary = secretaryRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("registro não encontrado"));
        secretaryRepository.delete(secretary);

    }

    //Metodo para registrar um paciente que não está cadastrado no sistema
    public void registerPatient(PatientDTO data, RequestTokenDTO dataT) {

        //Criando instâncias do adiministrador que está cadastrando e da clinica que ele está vinculado
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var secretary = secretaryRepository.findByAuthId_Id(UUID.fromString(id));
        Clinic clinic = clinicRepository.findById(secretary.getClinicId().getId()).orElseThrow(() -> new RuntimeException("Clinica não encontrada"));

        Auth newAuth = new Auth();
        Patient newPatient = new Patient();
        Role patient = roleRepository.findByName("Patient");

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
        newAuth.getRoles().add(patient);
        authRepository.save(newAuth);

        try {

            newPatient.setCpf(data.cpf());
            newPatient.setAuthId(newAuth);
            patientRepository.save(newPatient);

            //String tokenE = newAuth.getToken();
            //String url = "http://localhost:5173/firstLogin" + tokenE;

            //emailService.sendEmail(newAuth.getUsernameKey(), "Para ativar sua conta acesse esse link", url);

            clinic.getPatients().add(newPatient);
            clinicRepository.save(clinic);

        } catch (Exception e) {
            authRepository.delete(newAuth);
            e.printStackTrace();
            throw e;
        }


    }

    //Metodo para ver a clinica que a secretaria está cadastrada
    public ResponseSecretaryClinicDTO clinicSecretary(RequestTokenDTO dataT) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Auth auth = authRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
        Secretary secretary = secretaryRepository.findByAuthId_Id(auth.getId());
        Clinic clinic = clinicRepository.findById(secretary.getClinicId().getId()).orElseThrow(() -> new EntityNotFoundException("Clinica não encontrada"));
        return new ResponseSecretaryClinicDTO(clinic.getName());
    }

    //Metodo para ver se o paciente está cadastrado na clínica
    public boolean patientCli(GetPatientByCPFDTO data, RequestTokenDTO dataT) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var secretary = secretaryRepository.findByAuthId_Id(UUID.fromString(id));
        Clinic clinic = clinicRepository.findById(secretary.getClinicId().getId()).orElseThrow(() -> new EntityNotFoundException("Clinica não encontrada"));

        Patient patient = patientRepository.findByCpf(data.cpf());


        return clinicRepository.existsPatientClinic(clinic.getId(), patient.getId());

    }

    //Metodo para ver ser o paciente está cadastrado no sistema
    public boolean patientExists(GetPatientByCPFDTO data) {
        return patientRepository.existsByCpf(data.cpf());
    }

    //Metodo para cadastrar um paciente ja cadastrado no sistema em uma nova clinica
    public void registerPatExistsCli(GetPatientByCPFDTO data, RequestTokenDTO dataT) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var secretary = secretaryRepository.findByAuthId_Id(UUID.fromString(id));

        Clinic clinic = clinicRepository.findById(secretary.getClinicId().getId()).orElseThrow(() -> new EntityNotFoundException("Clinica não encontrada"));

        Patient patient = patientRepository.findByCpf(data.cpf());

        try {
            clinic.getPatients().add(patient);
            clinicRepository.save(clinic);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Metodo para consultar os pacientes de uma clinica pelo Id da secretaria logado
    public List<ResponsePatCliDTO> patCli (RequestTokenDTO dataT){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var secretary = secretaryRepository.findByAuthId_Id(UUID.fromString(id));
        Clinic clinic = clinicRepository.findById(secretary.getClinicId().getId()).orElseThrow(() -> new RuntimeException("Clinica não encontrada"));

        return clinicRepository.findPatByClinic(clinic.getId());

    }


    public Secretary updateSecretary(SecretaryDTO data, UUID uuid) {
        Secretary secretaryUpdate = secretaryRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("paciente não encontrado"));

        secretaryUpdate.setTelephone(data.telephone());

        return secretaryRepository.save(secretaryUpdate);

    }

    //Metodo para listar os médicos disponiveis para realizar uma consulta
    public List<ResponseDocCliConsultDTO> docCLiConsult (RequestTokenDTO dataT){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var secretary = secretaryRepository.findByAuthId_Id(UUID.fromString(id));
        Clinic clinic = clinicRepository.findById(secretary.getClinicId().getId()).orElseThrow(() -> new EntityNotFoundException("Clinica não encontrada"));

        return clinicRepository.findDocConsultByClinic(clinic.getId());

    }

    //Metodo para criar as consultas
    public void registerAppointment (RegisterAppointmentDTO data, RequestTokenDTO dataT){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var secretary = secretaryRepository.findByAuthId_Id(UUID.fromString(id));
        Clinic clinic = clinicRepository.findById(secretary.getClinicId().getId()).orElseThrow(() -> new EntityNotFoundException("Clinica não encontrada"));

        Optional<Auth> auth = authRepository.findAuthByUsernameKey(data.email());

        Doctor doctor = doctorRepository.findByAuthId_Id(auth.get().getId());

        Patient patient = patientRepository.findByCpf(data.cpf());

        if (!doctor.isAvailable()){
            System.out.println("deu erro");
        } else {
            Appointment newAppointment = new Appointment();
            newAppointment.setClinic(clinic);
            newAppointment.setPatient(patient);
            newAppointment.setDoctor(doctor);
            newAppointment.setDateCreate(LocalDateTime.now());
            newAppointment.setOpenAppointment(true);
            appointmentRepository.save(newAppointment);

            doctor.setAvailable(false);
            doctorRepository.save(doctor);
        }


    }


}
