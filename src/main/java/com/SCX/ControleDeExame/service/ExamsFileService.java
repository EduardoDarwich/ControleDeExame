package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseLabCliDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsRequestDTO.GetExamsRequestIdDTO;
import com.SCX.ControleDeExame.dataTransferObject.fileDTO.UploadDTO;
import com.SCX.ControleDeExame.domain.appointment.Appointment;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.consultation.Consultation;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.exams.Exams;
import com.SCX.ControleDeExame.domain.examsRequest.ExamsRequest;
import com.SCX.ControleDeExame.domain.examsFile.ExamsFile;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.domain.patient.Patient;
import com.SCX.ControleDeExame.domain.user_lab.UserLab;
import com.SCX.ControleDeExame.infra.security.TokenService;
import com.SCX.ControleDeExame.repository.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
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
import org.springframework.web.multipart.MultipartFile;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
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
    public void uploadFile(UploadDTO data, RequestTokenDTO dataT) throws IOException {
        // Garante que a pasta exista
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        UserLab userLab = userLabRepository.findByAuthId_Id(UUID.fromString(id));
        var auth = authRepository.findById(userLab.getAuthId().getId());
        Optional<Laboratory> laboratory = laboratoryRepository.findById(userLab.getLaboratoryId().getId());
        Optional<ExamsRequest> examsRequest = Optional.ofNullable(requestExamsRepository.findByCodVerific(data.examsReqId()));
        Optional<Consultation> consultation = consultationRepository.findById(examsRequest.get().getConsultation().getId());
        Optional<Appointment> appointment = appointmentRepository.findById(consultation.get().getAppointment().getId());
        Optional<Patient> patient = patientRepository.findById(appointment.get().getPatient().getId());
        Optional<Doctor> doctor = doctorRepository.findById(appointment.get().getDoctor().getId());
        List<Exams> exams = examsRequest.get().getExams();
        int count = exams.size();


        List<MultipartFile> files = data.file();

        for (MultipartFile file : files) {

            int contador = examsRequest.get().getCountExm();


            // Cria nome único com Ids
            String uniqueFilename = patient.get().getId() + "_" + doctor.get().getId() + "_" + laboratory.get().getId() + "_" + UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, uniqueFilename);


            // Salva o arquivo
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);


            examsRequest.get().setStatus("Entregue");


            // Salva os dados no banco
            ExamsFile entity = new ExamsFile();
            entity.setExamsRequest(examsRequest.get());
            entity.setPatient(patient.get());
            entity.setDoctor(doctor.get());
            entity.setLaboratory(laboratory.get());
            entity.setFileName(uniqueFilename);
            entity.setFilePath(filePath.toString());
            entity.setUploadDate(LocalDateTime.now());

            examsFileRepository.save(entity);

            examsRequest.get().setCountExm(contador + 1);
            requestExamsRepository.save(examsRequest.get());


        }
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
        Document document = new Document(PageSize.A4, 36, 36, 36, 36); // margens
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Optional<ExamsRequest> examsRequestOPT = requestExamsRepository.findById(UUID.fromString(data.id()));
        ExamsRequest examsRequest = examsRequestOPT.get();
        List<Exams> exams = examsRequest.getExams();
        Consultation consultation = examsRequest.getConsultation();
        Clinic clinic = consultation.getAppointment().getClinic();

        List<ResponseLabCliDTO> labs = clinicRepository.findLabByClinic(clinic.getId());

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // === FONTES ===
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 10);
            Font smallFont = new Font(Font.FontFamily.HELVETICA, 9);
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);

            // === CABEÇALHO ===
            PdfPTable header = new PdfPTable(2);
            header.setWidthPercentage(100);
            header.setWidths(new float[]{3, 2});

            PdfPCell prefeitura = new PdfPCell();
            prefeitura.setBorder(Rectangle.NO_BORDER);
            prefeitura.addElement(new Paragraph("PREFEITURA DE MOGI DAS CRUZES", headerFont));
            prefeitura.addElement(new Paragraph("SECRETARIA MUNICIPAL DE SAÚDE", smallFont));
            prefeitura.addElement(new Paragraph("UNIDADE DE ATENDIMENTO: UNIDADE 01", smallFont));
            prefeitura.addElement(new Paragraph("ENDEREÇO: RUA DOS PALMARES", smallFont));

            PdfPCell logoCell = new PdfPCell();
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            // Se quiser adicionar um logotipo:
            // Image logo = Image.getInstance("caminho/para/logo.png");
            // logo.scaleToFit(70, 70);
            // logoCell.addElement(logo);
            logoCell.addElement(new Paragraph(" ", normalFont)); // espaço reservado

            header.addCell(prefeitura);
            header.addCell(logoCell);
            document.add(header);

            // Linha separadora
            document.add(new Paragraph("\n"));
            LineSeparator line = new LineSeparator();
            line.setLineColor(BaseColor.GRAY);
            document.add(new Chunk(line));

            // === TÍTULO ===
            Paragraph title = new Paragraph("FICHA DE REQUISIÇÃO DE SERVIÇOS AUXILIARES E EXAMES", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingBefore(10);
            title.setSpacingAfter(10);
            document.add(title);

            // === INFORMAÇÕES GERAIS ===
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(8f);
            infoTable.setWidths(new float[]{2, 2});

            addCell(infoTable, "Data/Hora: " + LocalDateTime.now().format(fmt), normalFont);
            addCell(infoTable, "Nº do Atendimento: " + consultation.getAppointment().getConsultation().getExamsRequests().getCodVerific(), normalFont);

            document.add(infoTable);

            // === DADOS DO PACIENTE ===
            Paragraph patientSection = new Paragraph("Dados do Paciente", headerFont);
            patientSection.setSpacingBefore(5);
            document.add(patientSection);

            PdfPTable patientTable = new PdfPTable(3);
            patientTable.setWidthPercentage(100);
            patientTable.setWidths(new float[]{2, 2, 2});

            addCell(patientTable, "Nome: " + consultation.getAppointment().getPatient().getAuthId().getName().toUpperCase(), normalFont);
            addCell(patientTable, "CPF: " + consultation.getAppointment().getPatient().getCpf(), normalFont);
            addCell(patientTable, "Telefone: " + consultation.getAppointment().getPatient().getTelephone(), normalFont);


            document.add(patientTable);

            // === SEÇÃO DE SOLICITAÇÃO ===
            Paragraph sectionSolic = new Paragraph("Informações do Pedido", headerFont);
            sectionSolic.setSpacingBefore(10);
            document.add(sectionSolic);

            PdfPTable solicitTable = new PdfPTable(2);
            solicitTable.setWidthPercentage(100);
            solicitTable.setWidths(new float[]{3, 2});

            addCell(solicitTable, "Estabelecimento solicitante:  " + clinic.getName().toUpperCase(), normalFont);
            addCell(solicitTable, "Profissional Solicitante:   " + consultation.getAppointment().getDoctor().getAuthId().getName().toUpperCase(), normalFont);

            document.add(solicitTable);

            // === LISTA DE EXAMES ===
            Paragraph examsSection = new Paragraph("EXAMES / PROCEDIMENTOS SOLICITADOS", headerFont);
            examsSection.setSpacingBefore(10);
            examsSection.setSpacingAfter(5);
            document.add(examsSection);

            PdfPTable examsTable = new PdfPTable(3);
            examsTable.setWidthPercentage(100);
            examsTable.setWidths(new float[]{2, 3, 4});

            PdfPCell c1 = new PdfPCell(new Phrase("CID", headerFont));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setBackgroundColor(new BaseColor(200, 200, 200));
            examsTable.addCell(c1);

            PdfPCell c2 = new PdfPCell(new Phrase("EXAME / PROCEDIMENTO", headerFont));
            c2.setHorizontalAlignment(Element.ALIGN_CENTER);
            c2.setBackgroundColor(new BaseColor(200, 200, 200));
            examsTable.addCell(c2);

            PdfPCell c3 = new PdfPCell(new Phrase("JUSTIFICATIVA", headerFont));
            c3.setHorizontalAlignment(Element.ALIGN_CENTER);
            c3.setBackgroundColor(new BaseColor(200, 200, 200));
            examsTable.addCell(c3);

            for (Exams exams1 : exams) {
                addExamRow(examsTable, exams1.getCid(), exams1.getExamsType(), exams1.getJustify());
            }

            document.add(examsTable);


            // === NOVA SEÇÃO: LABORATÓRIOS DISPONÍVEIS ===
            Paragraph labSection = new Paragraph("LABORATÓRIOS DISPONÍVEIS", headerFont);
            labSection.setSpacingBefore(15);
            labSection.setSpacingAfter(5);
            document.add(labSection);

            // Novo parágrafo para as linhas
            Paragraph labsList = new Paragraph();
            for (ResponseLabCliDTO dataLab : labs) {
                addLabRow(labsList, dataLab.getName(), dataLab.getTelephone());
            }

            document.add(labsList);


            // === ASSINATURA DO MÉDICO ===
            document.add(new Paragraph("\n\n\n")); // espaço antes da linha

            // Cria uma linha para assinatura
            LineSeparator signatureLine = new LineSeparator();
            signatureLine.setLineColor(BaseColor.BLACK);
            signatureLine.setLineWidth(0.8f);
            signatureLine.setPercentage(40f); // tamanho da linha (40% da largura da página)
            signatureLine.setAlignment(Element.ALIGN_CENTER);

            document.add(new Chunk(signatureLine)); // adiciona a linha

            // Nome e CRM do médico
            Paragraph doctorInfo = new Paragraph(
                    consultation.getAppointment().getDoctor().getAuthId().getName().toUpperCase() + "\n" + "CRM: " + consultation.getAppointment().getDoctor().getCrm(),
                    new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)
            );
            doctorInfo.setAlignment(Element.ALIGN_CENTER);
            doctorInfo.setSpacingBefore(5);
            document.add(doctorInfo);

            // === RODAPÉ ===
            document.add(new Paragraph("\n"));
            document.add(new Chunk(line));
            Paragraph footer = new Paragraph(
                    "Gerado automaticamente pelo Sistema de Exames - " + LocalDateTime.now().format(fmt),
                    smallFont
            );
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(5);
            document.add(footer);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    // Mtodo auxiliar para adicionar células genéricas
    private void addCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        table.addCell(cell);
    }

    // Mtodo auxiliar para adicionar linha de exame
    private void addExamRow(PdfPTable table, String exam, String justification, String cid) {
        PdfPCell examCell = new PdfPCell(new Phrase(exam));
        examCell.setPadding(5);
        table.addCell(examCell);

        PdfPCell justCell = new PdfPCell(new Phrase(justification));
        justCell.setPadding(5);
        table.addCell(justCell);

        PdfPCell cidCell = new PdfPCell(new Phrase(cid));
        cidCell.setPadding(5);
        table.addCell(cidCell);
    }

    private void addLabRow(Paragraph paragraph, String name, String telephone) {
        if (telephone == null || telephone.isBlank()) {
            telephone = "Não informado";
        } else {
            // Remove tudo que não é número
            telephone = telephone.replaceAll("\\D", "");

            // Formata automaticamente
            if (telephone.length() == 11) {
                telephone = "(" + telephone.substring(0, 2) + ") " +
                        telephone.substring(2, 7) + "-" +
                        telephone.substring(7);
            } else if (telephone.length() == 10) {
                telephone = "(" + telephone.substring(0, 2) + ") " +
                        telephone.substring(2, 6) + "-" +
                        telephone.substring(6);
            }
        }

        Chunk nameChunk = new Chunk(name + " — ");
        Chunk telChunk = new Chunk("Contato: " + telephone);

        paragraph.add(nameChunk);
        paragraph.add(telChunk);
        paragraph.add(Chunk.NEWLINE); // quebra de linha
    }


}