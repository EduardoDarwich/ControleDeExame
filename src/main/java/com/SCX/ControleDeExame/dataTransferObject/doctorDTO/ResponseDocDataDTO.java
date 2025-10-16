package com.SCX.ControleDeExame.dataTransferObject.doctorDTO;

import com.SCX.ControleDeExame.domain.doctor.Doctor;

import java.util.UUID;

public record ResponseDocDataDTO(String crm, UUID id, UUID authId, String telephone) {
    public ResponseDocDataDTO(Doctor doctor){
        this(doctor.getCrm(), doctor.getId(), doctor.getAuthId().getId(), doctor.getTelephone());
    }
}
