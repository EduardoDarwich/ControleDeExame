package com.SCX.ControleDeExame.dataTransferObject.adminSystemDTO;

import lombok.Getter;

@Getter
public class ResponsePatSystDTO {
    private String name;
    private String status;
    private String email;

    public ResponsePatSystDTO(String name, boolean active, String email) {
        this.name = name;
        this.status = active ? "Ativo" : "Inativo";
        this.email = email;
    }
}
