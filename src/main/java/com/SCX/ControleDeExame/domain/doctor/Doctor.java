package com.SCX.ControleDeExame.domain.doctor;

import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.examsRequest.ExamsRequest;
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
    private boolean available;

    @Column(name = "id_clinic")
    private UUID idClinic;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "auth_id", nullable = false, unique = true)
    private Auth authId;

    @ManyToMany(mappedBy = "doctors", fetch = FetchType.EAGER)
    private List<Clinic> clinics = new ArrayList<>();

    @OneToMany(mappedBy =  "doctorId", fetch = FetchType.EAGER)
    private List<ExamsRequest> examsRequests = new ArrayList<>();

   /* @OneToMany(mappedBy = "doctorId")
    private Doctor doctor;*/

}
