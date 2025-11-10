package com.SCX.ControleDeExame.domain.examsRequest;

import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.consultation.Consultation;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.exams.Exams;
import com.SCX.ControleDeExame.domain.examsFile.ExamsFile;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.domain.patient.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    @Column(name = "cod_verific")
    private  String codVerific;
    @Column(name = "request_date")
    private LocalDateTime requestDate;
    @Column(name = "executed_date")
    private LocalDateTime executedDate;
    private String status;
    @Column(name = "count_exm")
    private int countExm;


    @ManyToOne()
    @JoinColumn(name = "doctor_id", nullable = false, unique = true)
    private Doctor doctorId;

    @ManyToOne()
    @JoinColumn(name = "clinic_id", nullable = false, unique = true)
    private Clinic clinicId;

    @ManyToOne()
    @JoinColumn(name = "patient_id", nullable = false, unique = true)
    private Patient patientId;

    @OneToOne()
    @JoinColumn(name = "consultation_id" , nullable = false, unique = true)
    private Consultation consultation;

    @OneToMany(mappedBy = "examsRequest", cascade = CascadeType.ALL)
    private List<Exams> exams = new ArrayList<>();

    @OneToMany(mappedBy = "examsRequest", cascade = CascadeType.ALL)
    private List<ExamsFile> examsFile = new ArrayList<>();

}

