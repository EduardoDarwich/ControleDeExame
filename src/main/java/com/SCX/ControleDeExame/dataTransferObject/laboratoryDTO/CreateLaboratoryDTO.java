package com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO;

public record CreateLaboratoryDTO(
        String name,
        String cnpj,
        String telephone,
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        String localidade,
        String uf

) {

}
