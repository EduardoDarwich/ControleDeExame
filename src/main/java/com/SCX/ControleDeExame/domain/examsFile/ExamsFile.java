package com.SCX.ControleDeExame.domain.examsFile;

import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.examsRequest.ExamsRequest;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.domain.patient.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "exams_file")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ExamsFile {
    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne()
    @JoinColumn(name = "exams_request_id")
    private ExamsRequest examsRequest;

    @OneToOne()
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @OneToOne()
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @OneToOne()
    @JoinColumn(name = "lab_id")
    private Laboratory laboratory;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

}