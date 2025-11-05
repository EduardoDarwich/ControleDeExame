package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.domain.filePath.FilePath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FilePathRepository extends JpaRepository<FilePath, UUID> {
}