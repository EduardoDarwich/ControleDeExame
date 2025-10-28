package com.SCX.ControleDeExame.dataTransferObject.cnpjVerifyDTO;

public record CnpjVerifyDTO(
        String nome,
        String fantasia,
        String situacao,
        String uf,
        String telefone
) {
}
