package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsRequestDTO.GetExamsRequestIdDTO;
import com.SCX.ControleDeExame.dataTransferObject.fileDTO.UploadDTO;
import com.SCX.ControleDeExame.domain.appointment.Appointment;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.consultation.Consultation;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.examsRequest.ExamsRequest;
import com.SCX.ControleDeExame.domain.examsFile.ExamsFile;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.domain.patient.Patient;
import com.SCX.ControleDeExame.domain.user_lab.UserLab;
import com.SCX.ControleDeExame.infra.security.TokenService;
import com.SCX.ControleDeExame.repository.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;

import com.itextpdf.text.Paragraph;

import com.itextpdf.text.Font;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExamsFileService {

    @Autowired
    ExamsFileRepository examsFileRepository;

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

    //Metodo para enviar o pdf de um exame para o sistema
    public ExamsFile uploadFile(UploadDTO data, RequestTokenDTO dataT) throws IOException {
        // Garante que a pasta exista
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        UserLab userLab = userLabRepository.findByAuthId_Id(UUID.fromString(id));
        var auth = authRepository.findById(userLab.getAuthId().getId());
        Optional<Laboratory> laboratory = laboratoryRepository.findById(userLab.getLaboratoryId().getId());

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
        ExamsFile entity = new ExamsFile();
        entity.setExamsRequest(examsRequest.get());
        entity.setPatient(patient.get());
        entity.setDoctor(doctor.get());
        entity.setLaboratory(laboratory.get());
        entity.setFileName(uniqueFilename);
        entity.setFilePath(filePath.toString());

        return examsFileRepository.save(entity);
    }

    //Metodo para Baixar o pdf ao clicar
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

    //Metodo para transformar a requisição de exames em um pdf(testar)
    public ByteArrayInputStream generateExamRequestPdf(GetExamsRequestIdDTO data) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Optional<ExamsRequest> examOPT = requestExamsRepository.findById(UUID.fromString(data.id()));
        ExamsRequest exam = examOPT.get();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            //Título principal
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Requisição de exame", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            //Subtítulo
            Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Paragraph subtitle = new Paragraph("Requisição de exame", subtitleFont);
            subtitle.setSpacingAfter(10);
            document.add(subtitle);

            //Tabela com informações
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            // Cada linha da tabela mostra um campo e seu valor
            table.addCell("ID do Pedido");
            table.addCell(exam.getId().toString());

            table.addCell("Tipo do Exame");
            table.addCell(exam.getExamType() != null ? exam.getExamType() : "-");

            table.addCell("Tipo da Amostra");
            table.addCell(exam.getSampleType() != null ? exam.getSampleType() : "-");

            table.addCell("Status");
            table.addCell(exam.getStatus() != null ? exam.getStatus() : "-");

            table.addCell("Complemento");
            table.addCell(exam.getComplement() != null ? exam.getComplement() : "-");

            table.addCell("Data do Pedido");
            table.addCell(exam.getRequestDate() != null ? exam.getRequestDate().format(fmt) : "-");


            document.add(table);

            //Espaçamento e rodapé
            document.add(new Paragraph("\n"));
            Paragraph footer = new Paragraph("Gerado automaticamente pelo sistema de exames.", new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC));
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

}