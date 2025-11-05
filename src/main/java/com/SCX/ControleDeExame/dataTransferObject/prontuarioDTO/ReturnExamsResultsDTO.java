package com.SCX.ControleDeExame.dataTransferObject.prontuarioDTO;

import com.SCX.ControleDeExame.domain.exams.Exams;

import java.util.List;

public record ReturnExamsResultsDTO(
        List<Exams> ExamsResults
) {
}
