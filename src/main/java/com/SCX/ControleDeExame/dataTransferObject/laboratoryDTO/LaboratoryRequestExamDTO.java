package com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO;

import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
public class LaboratoryRequestExamDTO {
    private String nameP;
    private UUID idReq;
    private String nameC;
    private String nameD;
    private String status;
    private String complement;
    private String typeEx;
    private String typeAm;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dateTime;

    public LaboratoryRequestExamDTO(String nameP, String nameC, String nameD, String status, String complement, String typeEx, String typeAm, LocalDateTime dateTime, UUID idReq){
        this.nameP = nameP;
        this.nameC = nameC;
        this.nameD = nameD;
        this.status = status;
        this.complement = complement;
        this.typeEx = typeEx;
        this.typeAm = typeAm;
        this.dateTime = dateTime;
        this.idReq = idReq;
    }
}
