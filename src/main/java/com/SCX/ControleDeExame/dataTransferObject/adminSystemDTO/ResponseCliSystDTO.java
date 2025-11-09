package com.SCX.ControleDeExame.dataTransferObject.adminSystemDTO;

import lombok.Getter;

@Getter
public class ResponseCliSystDTO {
    private String cnpj;
    private String name;
    private String status;

    public ResponseCliSystDTO(String name, String cnpj, boolean active){
        this.cnpj = cnpj;
        this.name = name;
        this.status = active ? "Ativo" : "Inativo";
    }
}
