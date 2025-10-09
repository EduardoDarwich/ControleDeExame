package com.SCX.ControleDeExame.domain.admin;

import com.SCX.ControleDeExame.domain.auth.Auth;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

//Classe representando a tabela "Admin"
@Table(name = "adimin")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Admin {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String email;
    private String cpf;
    private String telephone;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "auth_id", nullable = false, unique = true)
    private Auth auth_id;

}
