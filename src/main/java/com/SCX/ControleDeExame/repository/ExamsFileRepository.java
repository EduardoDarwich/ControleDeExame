package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.DoctorResultExamDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.ExamsFileDTO;
import com.SCX.ControleDeExame.domain.examsFile.ExamsFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ExamsFileRepository extends JpaRepository<ExamsFile, UUID> {
    List<ExamsFileDTO> findByPatient_Id(UUID id);
}