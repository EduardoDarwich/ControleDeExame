package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.domain.exams_type.Exams_type;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ExamsTypeRepository extends JpaRepository<Exams_type, UUID> {

}
