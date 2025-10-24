package com.SCX.ControleDeExame.domain.examsRequest;

import com.SCX.ControleDeExame.domain.appointment.Appointment;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.domain.patient.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    @Column(name = "exam_type" )
    private String examType;
    @Column(name = "sample_type")
    private String sampleType;
    private String complement;
    @Column(name = "request_date")
    private LocalDateTime requestDate;
    @Column(name = "executed_date")
    private LocalDateTime executedDate;
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

    @ManyToOne()
    @JoinColumn(name = "laboratory_id", nullable = false, unique = true)
    private Laboratory laboratoryId;

    @ManyToOne()
    @JoinColumn(name = "appointment_id" , nullable = false, unique = true)
    private Appointment appointmentId;
}

