package com.SCX.ControleDeExame.dataTransferObject.clinicDTO;

import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import lombok.Getter;

@Getter
public class ResponseLabCliDTO {
    private String cnpj;
    private String name;
    private String status;
    private String telephone;

    public ResponseLabCliDTO(String cnpj, String name, boolean active, String telephone){
        this.cnpj = cnpj;
        this.name = name;
        this.status = active ? "Ativo" : "Inativo";
        this.telephone = telephone;
    }

}
