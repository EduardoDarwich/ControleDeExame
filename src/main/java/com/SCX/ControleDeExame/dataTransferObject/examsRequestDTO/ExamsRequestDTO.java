package com.SCX.ControleDeExame.dataTransferObject.examsRequestDTO;



import java.util.UUID;

public record ExamsRequestDTO(String exam_type, String sample_type, String complement, String cpf, String cnpj) {

}
