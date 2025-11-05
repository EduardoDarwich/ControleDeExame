package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.fileDTO.UploadDTO;
import com.SCX.ControleDeExame.domain.filePath.FilePath;
import com.SCX.ControleDeExame.service.FilePathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    FilePathService filePathService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@ModelAttribute UploadDTO data, @RequestHeader("Authorization") RequestTokenDTO dataT) {
        try {
            FilePath savedFile = filePathService.uploadFile(data, dataT);
            return ResponseEntity.ok("Arquivo enviado com sucesso: " + savedFile.getOriginalName());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao enviar arquivo: " + e.getMessage());
        }
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String filename,
            @RequestParam(required = false) String displayName) throws IOException {
        return filePathService.downloadFile(filename);
    }
}

