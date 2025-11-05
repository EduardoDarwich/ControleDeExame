package com.SCX.ControleDeExame.dataTransferObject.prontuarioDTO;

import com.SCX.ControleDeExame.dataTransferObject.examsRequestDTO.ExamsRequestDTO;
import com.SCX.ControleDeExame.domain.examsRequest.ExamsRequest;

import java.util.List;

public record ReturnExamsRequestsDTO(

        List<ExamsRequestDTO> examsRequests
) {
}
