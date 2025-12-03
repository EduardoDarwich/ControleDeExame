package com.SCX.ControleDeExame.dataTransferObject.clinicDTO;

import lombok.Getter;

@Getter
public class ResponsePatCliDTO {

    private String name;
    private String status;
    private String telephone;

    public ResponsePatCliDTO(String name, boolean active, String telephone) {
        this.name = name;
        this.status = active ? "Ativo" : "Inativo";
        this.telephone= telephone;
    }
}
