package com.SCX.ControleDeExame.dataTransferObject.authDTO;

import com.SCX.ControleDeExame.dataTransferObject.roleDTO.RoleDTO;

import java.util.List;

public record PerfilDTO(String nome, List<RoleDTO> roles) {
}
