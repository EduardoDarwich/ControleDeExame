package com.SCX.ControleDeExame.domain.doctor;

import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
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
    private String crm;
    private String telephone;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "auth_id", nullable = false, unique = true)
    private Auth authId;

    @ManyToMany(mappedBy = "doctors", fetch = FetchType.EAGER)
    private List<Clinic> clinic = new ArrayList<>();

   /* @OneToMany(mappedBy = "doctorId")
    private Doctor doctor;*/

}
