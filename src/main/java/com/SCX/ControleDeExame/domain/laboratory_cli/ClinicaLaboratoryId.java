package com.SCX.ControleDeExame.domain.laboratory_cli;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ClinicaLaboratoryId implements Serializable {
    @Column(name = "id_clinic")
    private UUID idClinic;
    @Column(name = "id_laboratory")
    private UUID idLaboratory;
}
