package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LaboratoryRepository extends JpaRepository <Laboratory, UUID> {
    Laboratory findByCnpj(String cnpj);
}
