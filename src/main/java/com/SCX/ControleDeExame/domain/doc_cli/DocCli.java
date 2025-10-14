/*package com.SCX.ControleDeExame.domain.doc_cli;

import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "doc_cli")
@Entity
public class DocCli {
    @EmbeddedId
    private DocCliId id;

    @ManyToMany
    @MapsId("idDoctor")
    @JoinColumn(name = "id_doctor")

    private Doctor doctor;

    @ManyToMany
    @MapsId("idClinic")
    @JoinColumn(name = "id_clinic")
    private Clinic clinic;
}
*/