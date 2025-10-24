package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.domain.examsType.ExamsType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExamsTypeRepository extends JpaRepository<ExamsType, UUID> {

}
