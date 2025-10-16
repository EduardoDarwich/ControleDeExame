package com.SCX.ControleDeExame.domain.patient;

import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.examsRequest.ExamsRequest;
import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private Date date_birth;
    private String telephone;
    private String cpf;
    private String address;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "auth_id", nullable = false, unique = true)
    private Auth authId;

    @OneToMany(mappedBy = "patientId")
    private List<ExamsRequest> examsRequests = new ArrayList<>();

}
