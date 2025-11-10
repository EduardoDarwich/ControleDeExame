package com.SCX.ControleDeExame.dataTransferObject.patientDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class PatientRequestExamDTO {
    private String Id;
    private String nameD;
    private String nameC;
    private String status;
    private String complement;
    private String typeEx;
    private String typeAm;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dateTime;

    public PatientRequestExamDTO(String nameD, String nameC, String status, String complement, String typeEx, String typeAm, LocalDateTime dateTime, UUID Id) {
        this.nameD = nameD;
        this.nameC = nameC;
        this.status = status;
        this.complement = complement;
        this.typeEx = typeEx;
        this.typeAm = typeAm;
        this.dateTime = dateTime;
        this.Id = Id.toString();
    }
}

