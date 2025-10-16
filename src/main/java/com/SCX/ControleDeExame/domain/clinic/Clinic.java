package com.SCX.ControleDeExame.domain.clinic;

import com.SCX.ControleDeExame.domain.admin.Admin;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.examsRequest.ExamsRequest;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.domain.role.Role;
import com.SCX.ControleDeExame.domain.secretary.Secretary;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clinic")
@Entity
@EqualsAndHashCode(of = "id")
public class Clinic {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
    private String cnpj;
    private String address;
    private String telephone;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "laboratory_cli",
            joinColumns = @JoinColumn(name = "id_clinic"),
            inverseJoinColumns = @JoinColumn(name = "id_laboratory")
    )
    private List<Laboratory> laboratories = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "doc_cli",
            joinColumns = @JoinColumn(name = "id_clinic"),
            inverseJoinColumns = @JoinColumn(name = "id_doctor")
    )
    private List<Doctor> doctors = new ArrayList<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_cli",
            joinColumns = @JoinColumn(name = "id_clinic"),
            inverseJoinColumns = @JoinColumn(name = "id_user")
    )
    private List<Auth> users = new ArrayList<>();

    @OneToMany(mappedBy = "clinicId")
    private List<Admin> admins = new ArrayList<>();

    @OneToMany(mappedBy = "clinicId")
    private List<ExamsRequest> examsRequests = new ArrayList<>();

    @OneToMany(mappedBy = "clinicId")
    private List<Secretary> secretaries = new ArrayList<>();


}
