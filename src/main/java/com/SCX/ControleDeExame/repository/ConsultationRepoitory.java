package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.domain.consultation.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConsultationRepoitory extends JpaRepository<Consultation, UUID> {
}
