package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.fileDTO.UploadDTO;
import com.SCX.ControleDeExame.domain.appointment.Appointment;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.consultation.Consultation;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.examsRequest.ExamsRequest;
import com.SCX.ControleDeExame.domain.filePath.FilePath;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.domain.patient.Patient;
import com.SCX.ControleDeExame.domain.user_lab.UserLab;
import com.SCX.ControleDeExame.infra.security.TokenService;
import com.SCX.ControleDeExame.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
public class FilePathService {

    @Autowired
    FilePathRepository filePathRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    AuthRepository authRepository;

    @Autowired
    ClinicRepository clinicRepository;

    @Autowired
    LaboratoryRepository laboratoryRepository;

    @Autowired
    AppointmentRepository appointmentRepository;

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    UserLabRepository userLabRepository;

    @Autowired
    RequestExamsRepository requestExamsRepository;

    @Autowired
    ConsultationRepository consultationRepository;



    private final String uploadDir = "uploads";

    public FilePath uploadFile(UploadDTO data, RequestTokenDTO dataT) throws IOException {
        // Garante que a pasta exista
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        UserLab userLab = userLabRepository.findByAuthId_Id(UUID.fromString(id));
        var auth = authRepository.findById(userLab.getAuthId().getId());
        Optional<Laboratory> laboratory = laboratoryRepository.findById(userLab.getLaboratoryId().getId());
        System.out.println("chegou aqui" + data.examsReqId());
        Optional<ExamsRequest> examsRequest = requestExamsRepository.findById(UUID.fromString(data.examsReqId()));
        Optional<Consultation> consultation = consultationRepository.findById(examsRequest.get().getConsultation().getId());
        Optional<Appointment> appointment = appointmentRepository.findById(consultation.get().getAppointment().getId());
        Optional<Patient> patient = patientRepository.findById(appointment.get().getPatient().getId());
        Optional<Doctor> doctor = doctorRepository.findById(appointment.get().getDoctor().getId());

        // Cria nome único com IDs
        String uniqueFilename = patient.get().getId() + "_" + doctor.get().getId() + "_" + laboratory.get().getId() + "_" + UUID.randomUUID() + "_" + data.file().getOriginalFilename();
        Path filePath = Paths.get(uploadDir, uniqueFilename);



        // Salva o arquivo
        Files.copy(data.file().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Salva os dados no banco
        FilePath entity = new FilePath();
        entity.setExamsRequest(examsRequest.get());
        entity.setFileName(uniqueFilename);
        entity.setOriginalName(filePath.toString());

        return filePathRepository.save(entity);
    }

    public ResponseEntity<Resource> downloadFile(String filename) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(filename).normalize();

        String[] parts = filename.split("_");

        UUID pacienteId = UUID.fromString(parts[0]);
        String medicoId = parts[1];
        String laboratorioId = parts[2];

        Optional<Patient> patient = patientRepository.findById(pacienteId);
        Optional<Auth> auth = authRepository.findById(patient.get().getAuthId().getId());
        String nameP = auth.get().getName();
        String fileName = nameP.replaceAll("\\s+", "_") + ".pdf";




        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("Não foi possível ler o arquivo: " + filename);
        }

        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }


        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

}