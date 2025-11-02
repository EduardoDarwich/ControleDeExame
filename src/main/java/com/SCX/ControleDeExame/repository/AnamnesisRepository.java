package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.domain.anamnesis.Anamnesis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnamnesisRepository extends JpaRepository<Anamnesis, UUID> {
}
