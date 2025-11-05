package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseDocCliDTO;
import com.SCX.ControleDeExame.domain.anamnesis.Anamnesis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnamnesisRepository extends JpaRepository<Anamnesis, UUID> {
    @Query("SELECT a FROM Anamnesis a LEFT JOIN FETCH a.anamnesisCustom WHERE a.id = :id")
    Optional<Anamnesis> findByIdWithCustomFields(@Param("id") UUID id);
}
