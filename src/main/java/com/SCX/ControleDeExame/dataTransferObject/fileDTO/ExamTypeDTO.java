package com.SCX.ControleDeExame.dataTransferObject.fileDTO;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ExamTypeDTO(MultipartFile file, String examType) {
}
