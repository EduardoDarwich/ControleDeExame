package com.SCX.ControleDeExame.domain.examsRequest;

import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.patient.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "exams_request")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ExamsRequest {
    @Id
    @GeneratedValue
    private UUID id;
    private String exam_type;
    private String sample_type;
    private String complement;
    @Column(name = "request_date")
    private String requestDate;
    @Column(name = "executed_date")
    private String executedDate;
    private String status;


    @ManyToOne()
    @JoinColumn(name = "doctor_id", nullable = false, unique = true)
    private Doctor doctorId;

    @ManyToOne()
    @JoinColumn(name = "clinic_id", nullable = false, unique = true)
    private Clinic clinicId;

    @ManyToOne()
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    private Patient patientId;
}

