package com.SCX.ControleDeExame.dataTransferObject.clinicDTO;

import lombok.Getter;

@Getter
public class ResponseSecretaryCliDTO {
    private String name;
    private String status;
    private String email;

    public ResponseSecretaryCliDTO(String name, boolean active, String email){
        this.name = name;
        this.status = active ? "Ativo" : "Inativo";
        this.email = email;
    }
}
