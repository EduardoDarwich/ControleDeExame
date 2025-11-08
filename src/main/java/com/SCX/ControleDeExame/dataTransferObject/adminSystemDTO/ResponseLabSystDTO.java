package com.SCX.ControleDeExame.dataTransferObject.adminSystemDTO;

public class ResponseLabSystDTO {

    private String cnpj;
    private String name;
    private String status;

    public ResponseLabSystDTO(String cnpj, String name, boolean active){
        this.cnpj = cnpj;
        this.name = name;
        this.status = active ? "Ativo" : "Inativo";
    }
}
