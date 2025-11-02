package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.domain.anamnesis.AnamnesisCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AnamnesisCustomRepository extends JpaRepository<AnamnesisCustom, UUID> {
}
