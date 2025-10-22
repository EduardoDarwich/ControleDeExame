package com.SCX.ControleDeExame.domain.appointment;

import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.patient.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "appointment")
@Entity()
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Appointment {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "date_create")
    private LocalDateTime dateCreate;
    @Column(name = "open_appointment")
    private boolean openAppointment;
    @Column(name = "date_end")
    private LocalDateTime dateEnd;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_pat", nullable = false, unique = true)
    private Patient patient;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_doc", nullable = false, unique = true)
    private Doctor doctor;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_cli", nullable = false, unique = true)
    private Clinic clinic;
}
