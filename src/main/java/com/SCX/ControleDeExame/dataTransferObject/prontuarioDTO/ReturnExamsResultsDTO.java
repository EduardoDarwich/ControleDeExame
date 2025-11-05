package com.SCX.ControleDeExame.dataTransferObject.prontuarioDTO;

import com.SCX.ControleDeExame.dataTransferObject.patientDTO.ExamsFileDTO;
import com.SCX.ControleDeExame.domain.exams.Exams;
import com.SCX.ControleDeExame.domain.examsFile.ExamsFile;

import java.util.List;

public record ReturnExamsResultsDTO(
        List<ExamsFileDTO> ExamsResults
) {
}
