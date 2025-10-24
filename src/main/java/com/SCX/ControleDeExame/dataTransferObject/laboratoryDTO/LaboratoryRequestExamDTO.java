package com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO;

import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class LaboratoryRequestExamDTO {
    private String nameP;
    private String nameC;
    private String nameD;
    private String status;
    private String complement;
    private String typeEx;
    private String typeAm;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dateTime;

    public LaboratoryRequestExamDTO(String nameP, String nameC, String nameD, String status, String complement, String typeEx, String typeAm, LocalDateTime dateTime){
        this.nameP = nameP;
        this.nameC = nameC;
        this.nameD = nameD;
        this.status = status;
        this.complement = complement;
        this.typeEx = typeEx;
        this.typeAm = typeAm;
        this.dateTime = dateTime;
    }
}
