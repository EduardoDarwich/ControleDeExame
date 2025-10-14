package com.SCX.ControleDeExame.domain.laboratory;

import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.user_lab.UserLab;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

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
    private String cnpj;
    private String address;
    private String telephone;

    @OneToMany(mappedBy = "laboratoryId")
    private List<UserLab> userLabs = new ArrayList<>();

    @ManyToMany(mappedBy = "laboratories")
    private List<Clinic> clinic = new ArrayList<>();



}
