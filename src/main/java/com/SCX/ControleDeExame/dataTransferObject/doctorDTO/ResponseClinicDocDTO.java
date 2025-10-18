package com.SCX.ControleDeExame.dataTransferObject.doctorDTO;

import com.SCX.ControleDeExame.domain.clinic.Clinic;

public record ResponseClinicDocDTO(String name) {
    public ResponseClinicDocDTO(Clinic clinic){
        this(clinic.getName());
    }
}
