package com.SCX.ControleDeExame.dataTransferObject.doctorDTO;

import com.SCX.ControleDeExame.domain.clinic.Clinic;

public record ResponseClinicMedDTO(String name) {
    public ResponseClinicMedDTO(Clinic clinic){
        this(clinic.getName());
    }
}
