package com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO;

import org.springframework.web.multipart.MultipartFile;

public record UpdateExamDTO(MultipartFile file, String fileName, String examType) {
}
