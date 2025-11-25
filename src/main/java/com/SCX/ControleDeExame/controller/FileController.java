package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsRequestDTO.GetExamsRequestIdDTO;
import com.SCX.ControleDeExame.dataTransferObject.fileDTO.UploadDTO;
import com.SCX.ControleDeExame.domain.examsFile.ExamsFile;
import com.SCX.ControleDeExame.service.ExamsFileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    //Criando instancias utilizadas
    @Autowired
    ExamsFileService examsFileService;

    //Rota para fazer um upload de um pdf no sistema
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@ModelAttribute UploadDTO data, @RequestHeader("Authorization") RequestTokenDTO dataT) {
        try {
            examsFileService.uploadFile(data, dataT);
            return ResponseEntity.ok("Arquivo enviado com sucesso");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao enviar arquivo: " + e.getMessage());
        }
    }

    //Rota para fazer um download ao clicar
    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String filename,
            @RequestParam(required = false) String displayName) throws IOException {
        return examsFileService.downloadFile(filename);
    }

    //Rota para ter uma preview antes de baixar ao clicar
    @GetMapping("/preview/{filename}")
    public ResponseEntity<Resource> previewFile(@PathVariable String filename) throws IOException {
        final String uploadDir = "/app/uploads";
        Path filePath = Path.of(uploadDir, filename);

        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("Não foi possível ler o arquivo: " + filename);
        }

        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/pdf";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .body(resource);
    }

    //Rota para transformar a requisição de exames em pdf
    @PostMapping(value = "/examsRequestPDF", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> gerarPdf(@RequestBody GetExamsRequestIdDTO data) {
        try {

            ByteArrayInputStream pdfStream = examsFileService.generateExamRequestPdf(data);


            byte[] pdfBytes = pdfStream.readAllBytes();


            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("inline", "exame_" + data.id() + ".pdf");

            // Retorna o PDF
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Erro ao gerar PDF: " + e.getMessage()).getBytes());
        }
    }
}

