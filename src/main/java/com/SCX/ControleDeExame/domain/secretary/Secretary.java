package com.SCX.ControleDeExame.domain.secretary;

import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
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
    private String cpf;
    private String telephone;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "auth_id", nullable = false, unique = true)
    private Auth authId;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "clinic_id", nullable = false, unique = true)
    private Clinic clinicId;
}
