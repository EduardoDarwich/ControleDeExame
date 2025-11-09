package com.SCX.ControleDeExame.domain.exams;

import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.examsRequest.ExamsRequest;
import com.SCX.ControleDeExame.domain.examsType.ExamsType;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.domain.patient.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "exams")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode (of = "id")
public class Exams {
    @Id
    @GeneratedValue
    private UUID id;
    private String cid;
    private String justify;
    @Column(name = "exams_type_id")
    private String examsType;
    @ManyToOne()
    @JoinColumn(name = "request_id", nullable = false, unique = true)
    private ExamsRequest examsRequest;

}
