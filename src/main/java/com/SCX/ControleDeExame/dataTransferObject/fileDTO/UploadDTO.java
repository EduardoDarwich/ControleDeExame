package com.SCX.ControleDeExame.dataTransferObject.fileDTO;

import jakarta.mail.Multipart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record UploadDTO(List<ExamTypeDTO> file, String examsReqId) {
}
