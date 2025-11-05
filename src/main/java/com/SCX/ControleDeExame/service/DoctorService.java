package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.anamnesisDTO.*;
import com.SCX.ControleDeExame.dataTransferObject.appointmentDTO.GetAppointmentIdDTO;
import com.SCX.ControleDeExame.dataTransferObject.appointmentDTO.GetAppointmentOpenDocDTO;
import com.SCX.ControleDeExame.dataTransferObject.appointmentDTO.ReturnAppointmentsPatDTO;
import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.RequestNameClinicDTO;
import com.SCX.ControleDeExame.dataTransferObject.consultationDTO.CloseConsultationDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.*;
import com.SCX.ControleDeExame.dataTransferObject.examsRequestDTO.ExamsRequestDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsTypeDTO.ExamsTypeDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.ExamsFileDTO;
import com.SCX.ControleDeExame.dataTransferObject.prontuarioDTO.ResponseAnamnesisDTO;
import com.SCX.ControleDeExame.dataTransferObject.prontuarioDTO.ReturnDiagnosticDTO;
import com.SCX.ControleDeExame.dataTransferObject.prontuarioDTO.ReturnExamsRequestsDTO;
import com.SCX.ControleDeExame.dataTransferObject.prontuarioDTO.ReturnExamsResultsDTO;
import com.SCX.ControleDeExame.domain.anamnesis.Anamnesis;
import com.SCX.ControleDeExame.domain.anamnesis.AnamnesisCustom;
import com.SCX.ControleDeExame.domain.appointment.Appointment;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.consultation.Consultation;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.exams.Exams;
import com.SCX.ControleDeExame.domain.examsFile.ExamsFile;
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

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Autowired
    EmailService emailService;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    ExamsTypeRepository examsTypeRepository;

    @Autowired
    LogService logService;

    @Autowired
    ConsultationRepository consultationRepository;

    @Autowired
    AnamnesisRepository anamnesisRepository;

    @Autowired
    AnamnesisCustomRepository anamnesisCustomRepository;

    //Metodo para registrar um médico
    public void registerDoctor(CreateDoctorDTO data, RequestTokenDTO dataT) {

        //Criando instâncias do adiministrador que está cadastrando e da clinica que ele está vinculado
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var admin = adminRepository.findByAuthId_Id(UUID.fromString(id));
        var auth = authRepository.findById(admin.getAuthId().getId());
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
            newDoctor.setAvailable(true);
            newDoctor.setAuthId(newAuth);
            newDoctor.setSpecialty(data.specialty());
            newDoctor.setIdClinic(clinic.getId());
            doctorRepository.save(newDoctor);

            //String tokenE = newAuth.getToken();
            //String url = "http://localhost:5173/firstLogin" + tokenE;

            //emailService.sendEmail(newAuth.getUsernameKey(), "Para ativar sua conta acesse esse link", url);

            //Adicionadno o médico criado a clinica na qual ele está sendo cadastrado
            clinic.getDoctors().add(newDoctor);
            clinicRepository.save(clinic);
            logService.logAction(auth.get(), "Registrou um novo médico na clinica");
        } catch (Exception e) {
            clinic.getDoctors().remove(newDoctor);
            clinicRepository.save(clinic);

            authRepository.delete(newAuth);
            e.printStackTrace();
            throw e;
        }

    }

    //Metodo para cadastrar um medico que ja existe no sistema em uma clinica
    public void registerDocUserExists(DoctorVerificDTO data, RequestTokenDTO dataT) {

        //Criando instâncias do adiministrador que está cadastrando e da clinica que ele está vinculado
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var admin = adminRepository.findByAuthId_Id(UUID.fromString(id));
        var auth = authRepository.findById(admin.getAuthId().getId());
        Clinic clinic = clinicRepository.findById(admin.getClinicId().getId()).orElseThrow(() -> new RuntimeException("Clinica não encontrada"));

        Doctor docUser = doctorRepository.findByCrm(data.crm());

        try {
            clinic.getDoctors().add(docUser);
            clinicRepository.save(clinic);
            logService.logAction(auth.get(), "Registrou um novo médico na clinica");

        } catch (Exception e) {

            e.printStackTrace();
            throw e;
        }

    }

    //Metodo para verificar se o médico ja está cadastrado no sistema
    public boolean doctorVerific(DoctorVerificDTO data) {
        return doctorRepository.existsByCrm(data.crm());
    }

    //Metodo para devolver as clinicas que o médico está vinculado
    public List<ResponseClinicDocDTO> clinicsDoctor(RequestTokenDTO dataT) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Auth auth = authRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
        Doctor doctor = doctorRepository.findByAuthId_Id(auth.getId());

        return doctorRepository.findClinicByDoctor(doctor.getId());
    }

    //Metodo para verificar se o Medico está cadastrado na clinica
    public boolean verificDocCli(DoctorVerificDTO data, RequestTokenDTO dataT) {

        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var admin = adminRepository.findByAuthId_Id(UUID.fromString(id));
        Clinic clinic = clinicRepository.findById(admin.getClinicId().getId()).orElseThrow(() -> new RuntimeException("Clinica não encontrada"));

        Doctor doctor = doctorRepository.findByCrm(data.crm());

        return clinicRepository.existsDoctorClinic(clinic.getId(), doctor.getId());
    }

    //Metodo para alterar a clinica que o médico está "logado"
    public void updateClinicMed(RequestNameClinicDTO data, RequestTokenDTO dataT) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Doctor doctor = doctorRepository.findByAuthId_Id(UUID.fromString(id));
        var auth = authRepository.findById(doctor.getAuthId().getId());
        Clinic clinic = clinicRepository.findByName(data.name());

        String msg = "Alterou sua clínica ativa para " + clinic.getName();

        doctor.setIdClinic(clinic.getId());
        doctorRepository.save(doctor);
        logService.logAction(auth.get(), msg);
    }

    //Metodo para retornar a clinica ativa do medico
    public RequestNameClinicDTO clinicDocActive(RequestTokenDTO dataT) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Doctor doctor = doctorRepository.findByAuthId_Id(UUID.fromString(id));

        try {

            Optional<Clinic> clinic = clinicRepository.findById(doctor.getIdClinic());

            return new RequestNameClinicDTO(clinic.get().getName());

        } catch (Exception e) {
            throw new RuntimeException("Null");

        }


    }

    //Metodo para devolver o atendimento atual do medico
    public GetAppointmentOpenDocDTO returnOpenAppointment(RequestTokenDTO dataT) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Doctor doctor = doctorRepository.findByAuthId_Id(UUID.fromString(id));

        return appointmentRepository.findByDoctorAppointmentOpen(doctor.getId());
    }

    //Metodo para retornar os laboratórios disponiveis na clinica ativa do médico
    public List<ResponseDocCliLabDTO> LabByclinicDoc(RequestTokenDTO dataT) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Doctor doctor = doctorRepository.findByAuthId_Id(UUID.fromString(id));


        try {


            Optional<Clinic> clinic = clinicRepository.findById(doctor.getIdClinic());


            return doctorRepository.findLabByClinicDoc(doctor.getIdClinic());

        } catch (Exception e) {
            throw new RuntimeException("Null");

        }
    }

    //Metodo para encerrar um atendimento
    public void closeAppointment(Doctor doctor) {

        doctor.setAvailable(true);
        doctorRepository.save(doctor);

        Appointment appointment = appointmentRepository.findByDoctorAvaiable(doctor.getId());
        appointment.setDateEnd(LocalDateTime.now());
        appointment.setOpenAppointment(false);
        appointmentRepository.save(appointment);

    }

    //Metodo para retornar todos os tipos de exame
    public List<ExamsTypeDTO> getExamsType() {
        return examsTypeRepository.findAll().stream().map(ExamsTypeDTO::new).toList();
    }

    //Metodo para abrir uma consulta
    public void openConsultation (RequestTokenDTO dataT){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Auth auth = authRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
        Doctor doctor = doctorRepository.findByAuthId_Id(auth.getId());
        Appointment appointment = appointmentRepository.findByDoctorAvaiable(doctor.getId());
        Optional<Clinic> clinic = clinicRepository.findById(doctor.getIdClinic());

        Consultation newConsultation = new Consultation();
        newConsultation.setAppointment(appointment);
        newConsultation.setInit(LocalTime.now());
        consultationRepository.save(newConsultation);

    }

    //Metodo para fechar uma consulta
    public void closeConsultation (RequestTokenDTO dataT, CloseConsultationDTO data){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Auth auth = authRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
        Doctor doctor = doctorRepository.findByAuthId_Id(auth.getId());
        Appointment appointment = appointmentRepository.findByDoctorAvaiable(doctor.getId());
        Optional<Consultation> consultationOPT = consultationRepository.findById(appointment.getConsultation().getId());
        Consultation consultation = consultationOPT.get();

        LocalTime init = consultation.getInit();
        LocalTime close = LocalTime.now();
        long duracaoMinutos = Duration.between(init,close).toMinutes();


        consultation.setClosed(LocalTime.now());
        consultation.setReturns(data.returns());
        consultation.setDuration((int)duracaoMinutos);
        consultation.setFinished(true);
        consultation.setDiagnosis(data.diagnosis());
        consultationRepository.save(consultation);

        closeAppointment(doctor);
    }

    //Metodo para registrar uma anamnese
    public void registerNewAnamnese (RequestTokenDTO dataT, CreateAnamnesisDTO data){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Auth auth = authRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
        Doctor doctor = doctorRepository.findByAuthId_Id(auth.getId());
        Appointment appointment = appointmentRepository.findByDoctorAvaiable(doctor.getId());
        Optional<Consultation> consultationOPT = consultationRepository.findById(appointment.getConsultation().getId());
        Consultation consultation = consultationOPT.get();

        Anamnesis newAnamnesis = new Anamnesis();
        newAnamnesis.setConsultation(consultation);
        newAnamnesis.setDateCreate(LocalDate.now());
        newAnamnesis.setMainComplaint(data.mainComplaint());
        newAnamnesis.setHistoryOfCurrentIllness(data.historyOfCurrentIllness());
        newAnamnesis.setPersonalMedicalHistory(data.personalMedicalHistory());
        newAnamnesis.setFamilyHistory(data.familyHistory());
        newAnamnesis.setAllergies(data.allergies());
        newAnamnesis.setUseMedications(data.useMedications());
        newAnamnesis.setPreviousHospitalizations(data.previousHospitalizations());
        newAnamnesis.setPreviousSurgeries(data.previousSurgeries());
        newAnamnesis.setDiet(data.diet());
        newAnamnesis.setSleep(data.sleep());
        newAnamnesis.setPhysicalActivity(data.physicalActivity());
        newAnamnesis.setSmoking(data.smoking());
        newAnamnesis.setAlcoholism(data.alcoholism());
        newAnamnesis.setBloodPressure(data.bloodPressure());
        newAnamnesis.setHeartRate(data.heartRate());
        newAnamnesis.setTemperature(data.temperature());
        newAnamnesis.setWeight(data.weight());
        newAnamnesis.setHeight(data.height());
        newAnamnesis.setBmi(data.bmi());
        newAnamnesis.setObservations(data.observations());
        newAnamnesis.setDiagnosticHypothesis(data.diagnosticHypothesis());
        newAnamnesis.setTreatmentPlan(data.treatmentPlan());
        anamnesisRepository.save(newAnamnesis);
    }

    //Metodo para calcular o imc
    public ResultBmiDTO calculatorBmi (CalculatorBmiDTO data){
        double weight = data.weight();
        double height = data.height();

        return new ResultBmiDTO(weight / Math.pow(height,2));
    }

    //Metodo para retornar todos os atendimentos do paciente
    public List<ReturnAppointmentsPatDTO> getAppointmentPat (RequestTokenDTO dataT){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Doctor doctor = doctorRepository.findByAuthId_Id(UUID.fromString(id));
        Appointment appointment = appointmentRepository.findByDoctorAvaiable(doctor.getId());
        Patient patient = appointment.getPatient();

        return appointmentRepository.findAppointmentByPatient(patient.getId());
    }


    //Metodo para criar campos personalizados (testar)
    public void createCustomField (RequestTokenDTO dataT, List< CreateCustomFieldDTO> data){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Auth auth = authRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
        Doctor doctor = doctorRepository.findByAuthId_Id(auth.getId());
        Appointment appointment = appointmentRepository.findByDoctorAvaiable(doctor.getId());
        Optional<Consultation> consultationOPT = consultationRepository.findById(appointment.getConsultation().getId());
        Consultation consultation = consultationOPT.get();
        Optional<Anamnesis> anamnesisOPT = anamnesisRepository.findById(consultation.getAnamnesis().getId());
        Anamnesis anamnesis = anamnesisOPT.get();

        data.forEach(item -> {
            AnamnesisCustom newAnamnesisCustom = new AnamnesisCustom();
            newAnamnesisCustom.setAnamnesis(anamnesis);
            newAnamnesisCustom.setFieldValue(item.fieldValue());
            newAnamnesisCustom.setFieldName(item.fieldName());
            anamnesisCustomRepository.save(newAnamnesisCustom);
        });

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

    /*public List<GetByDoctorDTO> getExamsByDoctor(RequestTokenDTO data) {
        var idC = data.toString().replace("RequestTokenDTO[Token=", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Doctor doctorId = doctorRepository.findByAuthId_Id(UUID.fromString(id));
        return examsRepository.findAllByDoctorId(doctorId.getId());
    }*/

    //Metodo para fazer a requisição de um exame
    public void requestExams(ExamsRequestDTO data, RequestTokenDTO dataT) {
        try {
            var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
            var id = tokenService.registerUser(idC);
            Doctor doctor = doctorRepository.findByAuthId_Id(UUID.fromString(id));
            var auth = authRepository.findById(doctor.getAuthId().getId());
            Optional<Clinic> clinic = clinicRepository.findById(doctor.getIdClinic());
            Laboratory laboratory = laboratoryRepository.findByName(data.name());
            Appointment appointment = appointmentRepository.findByDoctorAvaiable(doctor.getId());
            Optional<Patient> patient = patientRepository.findById(appointment.getPatient().getId());

            String msg = "Fez um novo pedido de exame para o laboratório" + laboratory.getName();

            ExamsRequest newExamRequest = new ExamsRequest();
            newExamRequest.setDoctorId(doctor);
            newExamRequest.setClinicId(clinic.get());
            newExamRequest.setPatientId(patient.get());
            newExamRequest.setLaboratoryId(laboratory);
            newExamRequest.setConsultation(appointment.getConsultation());
            newExamRequest.setExamType(data.exam_type());
            newExamRequest.setSampleType(data.sample_type());
            newExamRequest.setStatus("Pendente");
            newExamRequest.setComplement(data.complement());
            newExamRequest.setRequestDate(LocalDateTime.now());
            requestExamsRepository.save(newExamRequest);


            logService.logAction(auth.get(), msg);



        } catch (Exception e) {
            e.printStackTrace();
            throw e;

        }
    }

    //Metodo para retornar as devoluções dos exames
    public List<DoctorResultExamDTO> doctorResultExam(RequestTokenDTO dataT) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Doctor doctor = doctorRepository.findByAuthId_Id(UUID.fromString(id));

        return doctorRepository.findByResultExamDoctor(doctor.getId());
    }

    //Metodo para retornar todas as requisições de exame pendentes do medico
    public List<DoctorRequestExamDTO> doctorRequestExam(RequestTokenDTO dataT) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Doctor doctor = doctorRepository.findByAuthId_Id(UUID.fromString(id));

        return doctorRepository.findRequestExamByDoctor(doctor.getId());
    }

    //Metodo para procurar exames por status
    public void getByExamsStatus() {
    }

    //Metodo para buscar exames associados a um paciente
    public void getByPatientName() {
    }

    //Metodo para verificar se o medico está em consulta ou não
    public boolean verifyDocIsConsult (RequestTokenDTO dataT){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Doctor doctor = doctorRepository.findByAuthId_Id(UUID.fromString(id));

        return doctorRepository.findDocIsConsult(doctor.getId());
    }

    //Metodo para retornar a anamnese da consulta(testar)
    public ResponseAnamnesisDTO getAnamneseByConsult (GetAppointmentIdDTO data){
        Optional<Appointment> appointment = appointmentRepository.findById(UUID.fromString(data.id()));
        Optional<Consultation> consultation = consultationRepository.findById(appointment.get().getConsultation().getId());
        Optional<Anamnesis> anamnesisOPT = anamnesisRepository.findByIdWithCustomFields(consultation.get().getAnamnesis().getId());
        Anamnesis anamnesis = anamnesisOPT.get();
        List<CreateCustomFieldDTO> createCustomFieldDTOS = anamnesis.getAnamnesisCustom()
                .stream()
                .map(cf -> new CreateCustomFieldDTO(
                        cf.getFieldName(),
                        cf.getFieldValue()
                )).toList();

        return new ResponseAnamnesisDTO(
                anamnesis.getMainComplaint(),
                anamnesis.getHistoryOfCurrentIllness(),
                anamnesis.getPersonalMedicalHistory(),
                anamnesis.getFamilyHistory(),
                anamnesis.getAllergies(),
                anamnesis.getUseMedications(),
                anamnesis.getPreviousHospitalizations(),
                anamnesis.getPreviousSurgeries(),
                anamnesis.getDiet(),
                anamnesis.getSleep(),
                anamnesis.getPhysicalActivity(),
                anamnesis.isSmoking(),
                anamnesis.isAlcoholism(),
                anamnesis.getBloodPressure(),
                anamnesis.getHeartRate(),
                anamnesis.getTemperature(),
                anamnesis.getWeight(),
                anamnesis.getHeight(),
                anamnesis.getBmi(),
                anamnesis.getObservations(),
                anamnesis.getDiagnosticHypothesis(),
                anamnesis.getTreatmentPlan(),
                createCustomFieldDTOS
        );

    }

    //Metodo para retornar o diagnostico relacionado a consulta se houver (testar)
    public ReturnDiagnosticDTO returnDiagnostic (GetAppointmentIdDTO data){
        Optional<Appointment> appointment = appointmentRepository.findById(UUID.fromString(data.id()));
        Optional<Consultation> consultation = consultationRepository.findById(appointment.get().getConsultation().getId());

        return new ReturnDiagnosticDTO(consultation.get().getDiagnosis());
    }

    //Metodo para retornar os pedidos de exames relacinados a consulta se houver (testar)
    public ReturnExamsRequestsDTO returnExamsRequests (GetAppointmentIdDTO data){
        Optional<Appointment> appointment = appointmentRepository.findById(UUID.fromString(data.id()));
        Optional<Consultation> consultation = consultationRepository.findById(appointment.get().getConsultation().getId());

        List<ExamsRequestDTO> examsRequestDTOS = consultation.get().getExamsRequests()
                .stream()
                .map(er -> new ExamsRequestDTO(
                        er.getExamType(),
                        er.getSampleType(),
                        er.getComplement(),
                        "mano ignora isso"
                )).toList();
        return new ReturnExamsRequestsDTO (examsRequestDTOS);


    }

    //Metodo para retornar os resutados dos exames relacionados a consulta se houver (testar)
    public ReturnExamsResultsDTO returnExamsResults (GetAppointmentIdDTO data){
        Optional<Appointment> appointment = appointmentRepository.findById(UUID.fromString(data.id()));
        Optional<Consultation> consultation = consultationRepository.findById(appointment.get().getConsultation().getId());

        List<ExamsFileDTO> ExamsFile = consultation.get().getExamsRequests()
                .stream()
                .map(er -> new ExamsFileDTO(
                        er.getExamsFile().getFileName()

                )).toList();

        return new ReturnExamsResultsDTO(ExamsFile);
    }




}