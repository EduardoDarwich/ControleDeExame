package com.SCX.ControleDeExame.domain.laboratory;

import com.SCX.ControleDeExame.domain.auth.Auth;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

//Classe representando a tabela "laboratory"
@Entity
@Table(name = "laboratory")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Laboratory {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String email;
    private String cnpj;
    private String address;
    private String telephone;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "auth_id", nullable = false, unique = true)
    private Auth authId;
}
