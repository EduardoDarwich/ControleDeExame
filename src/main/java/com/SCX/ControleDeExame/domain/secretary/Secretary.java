package com.SCX.ControleDeExame.domain.secretary;

import com.SCX.ControleDeExame.domain.auth.Auth;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

//Classe representando a tabela "secretary"
@Entity
@Table(name = "secretary")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Secretary {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String email;
    private String cpf;
    private String telephone;
    private String sector;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "auth_id", nullable = false, unique = true)
    private Auth authId;
}
