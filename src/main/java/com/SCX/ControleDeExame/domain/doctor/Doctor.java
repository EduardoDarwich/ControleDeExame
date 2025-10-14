package com.SCX.ControleDeExame.domain.doctor;

import com.SCX.ControleDeExame.domain.auth.Auth;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

//Classe representando a tabela "doctor"
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doctor")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Doctor {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String email;
    private String crm;
    private String telephone;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "auth_id", nullable = false, unique = true)
    private Auth authId;

   /* @OneToMany(mappedBy = "doctorId")
    private Doctor doctor;*/

}
