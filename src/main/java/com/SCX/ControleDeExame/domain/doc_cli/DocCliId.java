package com.SCX.ControleDeExame.domain.doc_cli;

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
public class DocCliId implements Serializable {
    @Column(name = "id_doctor")
    private UUID idDoctor;

    @Column(name = "id_clinic")
    private UUID idClinic;
}
