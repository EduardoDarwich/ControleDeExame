package com.SCX.ControleDeExame.dataTransferObject.patientDTO;

import java.time.LocalDate;

public record PatientDTO(String name, LocalDate date_birth, String telephone, String cpf, String email, String address, String password_key) {
}
