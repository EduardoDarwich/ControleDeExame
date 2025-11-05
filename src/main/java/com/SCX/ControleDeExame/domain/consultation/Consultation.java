package com.SCX.ControleDeExame.domain.consultation;

import com.SCX.ControleDeExame.domain.anamnesis.Anamnesis;
import com.SCX.ControleDeExame.domain.appointment.Appointment;
import com.SCX.ControleDeExame.domain.examsRequest.ExamsRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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
    private Integer duration;
    private boolean returns;
    private boolean finished;

    @OneToOne(mappedBy = "consultation")
    private Anamnesis anamnesis;

    @OneToMany(mappedBy = "consultation")
    private List<ExamsRequest> examsRequests = new ArrayList<>();

}
