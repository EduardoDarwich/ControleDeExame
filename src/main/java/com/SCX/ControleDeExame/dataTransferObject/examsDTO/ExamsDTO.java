package com.SCX.ControleDeExame.dataTransferObject.examsDTO;


import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record ExamsDTO(MultipartFile file, String examsReqId) {


}
