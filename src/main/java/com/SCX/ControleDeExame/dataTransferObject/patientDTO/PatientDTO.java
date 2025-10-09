package com.SCX.ControleDeExame.dataTransferObject.patientDTO;

public record PatientDTO(String name, long date_birth, String telephone, String cpf, String email, String address, String password_key) {
}
