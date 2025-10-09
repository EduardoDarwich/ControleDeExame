package com.SCX.ControleDeExame.domain.patient;

import com.SCX.ControleDeExame.domain.auth.Auth;
import jakarta.persistence.*;
import lombok.*;


import java.util.Date;
import java.util.UUID;

//Classe representando a tabela "patient"
@Table(name = "patient")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Patient {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private Date date_birth;
    private String telephone;
    private String cpf;
    private String email;
    private String address;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "auth_id", nullable = false, unique = true)
    private Auth auth_id;


}
