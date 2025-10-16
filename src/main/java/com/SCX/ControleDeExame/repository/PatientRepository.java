package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.domain.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository <Patient, UUID> {
    Patient findByCpf(String cpf);
}
