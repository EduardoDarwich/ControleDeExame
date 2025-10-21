package com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO;

import com.SCX.ControleDeExame.domain.clinic.Clinic;

public record ResponseClinicLabDTO(String name, String cnpj) {
    public ResponseClinicLabDTO(Clinic clinic){
        this(clinic.getName(), clinic.getCnpj());
    }
}
