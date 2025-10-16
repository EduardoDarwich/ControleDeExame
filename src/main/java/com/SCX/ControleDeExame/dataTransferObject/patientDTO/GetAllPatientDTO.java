package com.SCX.ControleDeExame.dataTransferObject.patientDTO;

import com.SCX.ControleDeExame.domain.patient.Patient;

public record GetAllPatientDTO( String address) {

    public GetAllPatientDTO (Patient patient) {

        this( patient.getAddress());
    }
}
