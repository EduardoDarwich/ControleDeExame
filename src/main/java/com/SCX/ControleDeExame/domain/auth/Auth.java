package com.SCX.ControleDeExame.domain.auth;

import com.SCX.ControleDeExame.domain.admin.Admin;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.domain.patient.Patient;
import com.SCX.ControleDeExame.domain.role.Role;
import com.SCX.ControleDeExame.domain.role.RoleEnum;
import com.SCX.ControleDeExame.domain.user_lab.UserLab;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

//Classe representando a tabela "auth"
@Table(name = "auth")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Auth implements UserDetails {



    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "username_key")
    private String usernameKey;
    private String password_key;
    private String name;
    private String token;
    private Boolean active;
    private Timestamp data_expiration_token;
    private Boolean token_status;
    private Boolean locked;
    @Column(name = "failed_attempts")
    private int failedAttempts;
    @Column(name = "lock_time")
    private LocalDateTime lockTime;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_role")
    )
    private Set<Role> roles = new HashSet<>();


    public Auth (String usernameKey, String name, String password_key,  String token, Boolean active, Timestamp data_expiration_token, Boolean token_status){
        this.usernameKey = usernameKey;
        this.name = name;
        this.password_key = password_key;
        this.token = token;
        this.active = active;
        this.data_expiration_token = data_expiration_token;
        this.token_status = token_status;
    }

//Metodo do Spring security para configurar as permissões de cada role
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

    }

//Metodo do Spring security para configurar qual é o atributo que representa a senha
    @Override
    public String getPassword() {
        return password_key;
    }

//Metodo do Spring security para configurar qual é o atributo que representa o username
    @Override
    public String getUsername() {
        return usernameKey;
    }
}
