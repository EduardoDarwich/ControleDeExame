package com.SCX.ControleDeExame.dataTransferObject.fileDTO;

import jakarta.mail.Multipart;
import org.springframework.web.multipart.MultipartFile;

public record UploadDTO(MultipartFile file, String examsReqId) {
}
