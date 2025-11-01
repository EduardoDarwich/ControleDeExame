package com.SCX.ControleDeExame.dataTransferObject.profileDTO;

import java.time.LocalDate;

public record ProfilePatientDTO (LocalDate dateBirth, String telephone, String cpf, String email, String name) {
}
