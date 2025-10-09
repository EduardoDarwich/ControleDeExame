package com.SCX.ControleDeExame.domain.exams;

import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.examsRequest.ExamsRequest;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.domain.patient.Patient;
import jakarta.persistence.*;
import lombok.*;

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
    private String status;
    private String result_value;
    private String result_file_url;
    private String observation;


    @OneToOne()
    @JoinColumn(name = "request_id", nullable = false, unique = true)
    private ExamsRequest requestId;

    @ManyToOne()
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    private Patient patientId;

    @ManyToOne()
    @JoinColumn(name = "laboratory_id", nullable = false, unique = true)
    private Laboratory laboratoryId;

    @ManyToOne()
    @JoinColumn(name = "doctor_id", nullable = false, unique = true)
    private Doctor doctor;

}
