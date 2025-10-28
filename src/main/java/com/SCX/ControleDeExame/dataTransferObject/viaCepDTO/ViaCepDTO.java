package com.SCX.ControleDeExame.dataTransferObject.viaCepDTO;

public record ViaCepDTO(
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        String localidade,
        String uf

) {}
