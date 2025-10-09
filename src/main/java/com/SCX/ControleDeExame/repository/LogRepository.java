package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.domain.log.Log;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LogRepository extends JpaRepository<Log, UUID> {
}
