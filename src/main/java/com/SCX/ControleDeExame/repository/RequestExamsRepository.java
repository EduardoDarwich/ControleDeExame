package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.domain.examsRequest.ExamsRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RequestExamsRepository extends JpaRepository<ExamsRequest, UUID> {
}
