package com.SCX.ControleDeExame.domain.anamnesis;

import com.SCX.ControleDeExame.domain.consultation.Consultation;
import com.SCX.ControleDeExame.service.SecretaryService;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "anamnesis")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Anamnesis {
    //identificação
    @Id
    @GeneratedValue
    private UUID id;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "consultation_id", nullable = false, unique = true)
    private Consultation consultation;
    @Column(name = "date_create")
    private LocalDate dateCreate;

    //queixa principal e doença atual
    @Column(name = "main_complaint")
    private String mainComplaint;
    @Column(name = "history_of_current_illness")
    private String historyOfCurrentIllness;
    @Column(name = "personal_medical_history")
    private String personalMedicalHistory;
    @Column(name = "family_history")
    private String familyHistory;
    private String allergies;
    @Column(name = "use_medications")
    private String useMedications;
    @Column(name = "previous_hospitalizations")
    private String previousHospitalizations;
    @Column(name = "previous_surgeries")
    private String previousSurgeries;

    //hábitos e estilo de vida
    private String diet;
    private String sleep;
    @Column(name = "physical_activity")
    private String physicalActivity;
    private boolean smoking;
    private boolean alcoholism;

    //exame físico
    @Column(name = "blood_pressure")
    private String bloodPressure;
    @Column(name = "heart_rate")
    private String heartRate;
    private double temperature;
    private double weight;
    private double height;
    private double bmi;

    //observações complementares e tratamento inicial
    private String observations;
    @Column(name = "diagnostic_hypothesis")
    private String diagnosticHypothesis;
    @Column(name = "treatment_plan")
    private String treatmentPlan;

    @OneToMany(mappedBy = "anamnesis")
    private List<AnamnesisCustom> anamnesisCustom = new ArrayList<>();





}
