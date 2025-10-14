/*package com.SCX.ControleDeExame.domain.laboratory_cli;

import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "laboratory_cli")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LaboratoryCli {

    @EmbeddedId
    private ClinicaLaboratoryId id;

    @ManyToMany
    @MapsId("idLaboratory")
    @JoinColumn(name = "id_laboratory")
    private Laboratory laboratory;

    @ManyToMany
    @MapsId("idClinic")
    @JoinColumn(name = "id_clinic")
    private Clinic clinc;
}
*/