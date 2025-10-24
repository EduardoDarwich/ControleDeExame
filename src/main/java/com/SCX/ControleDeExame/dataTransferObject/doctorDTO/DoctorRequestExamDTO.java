package com.SCX.ControleDeExame.dataTransferObject.doctorDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DoctorRequestExamDTO {
    private String nameP;
    private String nameC;
    private String nameL;
    private String status;
    private String complement;
    private String typeEx;
    private String typeAm;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dateTime;

    public DoctorRequestExamDTO(String nameP, String nameC, String nameL, String status, String complement, String typeEx, String typeAm, LocalDateTime dateTime){
        this.nameP = nameP;
        this.nameC = nameC;
        this.nameL = nameL;
        this.status = status;
        this.complement = complement;
        this.typeEx = typeEx;
        this.typeAm = typeAm;
        this.dateTime = dateTime;
    }

}
