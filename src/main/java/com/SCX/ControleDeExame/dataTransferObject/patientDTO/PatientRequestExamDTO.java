package com.SCX.ControleDeExame.dataTransferObject.patientDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PatientRequestExamDTO {
    private String nameD;
    private String nameC;
    private String nameL;
    private String status;
    private String complement;
    private String typeEx;
    private String typeAm;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dateTime;

    public PatientRequestExamDTO(String nameD, String nameC, String nameL, String status, String complement, String typeEx, String typeAm, LocalDateTime dateTime) {
        this.nameD = nameD;
        this.nameC = nameC;
        this.nameL = nameL;
        this.status = status;
        this.complement = complement;
        this.typeEx = typeEx;
        this.typeAm = typeAm;
        this.dateTime = dateTime;
    }
}

