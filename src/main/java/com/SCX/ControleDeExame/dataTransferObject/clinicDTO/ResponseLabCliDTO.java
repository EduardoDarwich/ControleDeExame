package com.SCX.ControleDeExame.dataTransferObject.clinicDTO;

import com.SCX.ControleDeExame.domain.laboratory.Laboratory;

public record ResponseLabCliDTO(String cnpj, String name) {
    public ResponseLabCliDTO(Laboratory laboratory){this(laboratory.getName(), laboratory.getCnpj());}
}
