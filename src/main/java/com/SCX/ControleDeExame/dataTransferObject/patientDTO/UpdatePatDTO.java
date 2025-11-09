package com.SCX.ControleDeExame.dataTransferObject.patientDTO;

import java.time.LocalDate;

public record UpdatePatDTO(String telephone, LocalDate birth) {
}
