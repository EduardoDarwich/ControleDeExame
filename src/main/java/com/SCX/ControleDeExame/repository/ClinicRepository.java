package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.domain.clinic.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClinicRepository extends JpaRepository<Clinic, UUID> {
    Clinic findByCnpj(String cnpj);
}
