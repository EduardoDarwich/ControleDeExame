package com.SCX.ControleDeExame.dataTransferObject.clinicDTO;

public record CreateClinicDTO(
        String name,
        String cnpj,
        String address,
        String telephone,
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        String localidade,
        String uf
) {
}
