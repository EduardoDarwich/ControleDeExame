package com.SCX.ControleDeExame.dataTransferObject.patientDTO;

import com.SCX.ControleDeExame.domain.patient.Patient;

public record GetAllPatientDTO(String email, String name, String address) {

    public GetAllPatientDTO (Patient patient) {

        this(patient.getEmail(), patient.getName(), patient.getAddress());
    }
}
