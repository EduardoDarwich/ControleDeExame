package com.SCX.ControleDeExame.domain.user_lab;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class UserLabId implements Serializable {
    @Column(name = "id_user")
    private UUID idUser;
    @Column(name = "id_laboratory")
    private UUID idLaboratory;
}
