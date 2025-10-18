package com.SCX.ControleDeExame.dataTransferObject.clinicDTO;

import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.doctor.Doctor;

public record ResponseDocCliDTO(String crm, String name) {
    public ResponseDocCliDTO(Doctor doctor, Auth auth){
        this(doctor.getCrm(), auth.getName());
    }
}
