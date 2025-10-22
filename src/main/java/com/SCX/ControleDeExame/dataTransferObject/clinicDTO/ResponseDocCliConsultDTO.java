package com.SCX.ControleDeExame.dataTransferObject.clinicDTO;

import lombok.Getter;

@Getter
public class ResponseDocCliConsultDTO {
    private String name;
    private String status;
    private String email;

    public ResponseDocCliConsultDTO(String name, boolean status, String email){
        this.name = name;
        this.status = status ? "Disponivel" : "Indisponivel";
        this.email = email;
    }
}
