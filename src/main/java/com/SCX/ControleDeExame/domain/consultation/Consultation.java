package com.SCX.ControleDeExame.domain.consultation;

import com.SCX.ControleDeExame.domain.anamnesis.Anamnesis;
import com.SCX.ControleDeExame.domain.appointment.Appointment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.UUID;

@Table(name = "consultation")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Consultation {
    @Id
    @GeneratedValue
    private UUID id;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;
    private LocalTime init;
    private LocalTime closed;
    private LocalTime duration;
    private boolean returns;
    private boolean finished;

    @OneToOne(mappedBy = "consultation")
    private Anamnesis anamnesis;

}
