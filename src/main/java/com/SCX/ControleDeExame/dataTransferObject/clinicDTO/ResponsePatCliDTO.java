package com.SCX.ControleDeExame.dataTransferObject.clinicDTO;

import lombok.Getter;

@Getter
public class ResponsePatCliDTO {

    private String name;
    private String status;

    public ResponsePatCliDTO(String name, boolean active) {
        this.name = name;
        this.status = active ? "Ativo" : "Inativo";
    }
}
