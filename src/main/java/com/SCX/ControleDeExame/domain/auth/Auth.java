package com.SCX.ControleDeExame.domain.auth;

import com.SCX.ControleDeExame.domain.roleEnum.RoleEnum;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

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
    private RoleEnum role;
    private String token;
    private Boolean status;
    private Timestamp data_expiration_token;
    private Boolean token_status;

    public Auth (String usernameKey, String password_key, RoleEnum role, String token, Boolean status, Timestamp data_expiration_token, Boolean token_status){
        this.usernameKey = usernameKey;
        this.password_key = password_key;
        this.role = role;
        this.token = token;
        this.status = status;
        this.data_expiration_token = data_expiration_token;
        this.token_status = token_status;
    }
//Metodo do Spring security para configurar as permissões de cada role
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == RoleEnum.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if (this.role == RoleEnum.DOCTOR) {
            return List.of(new SimpleGrantedAuthority("ROLE_DOCTOR"));
        } else if (this.role == RoleEnum.LABORATORY) {
            return List.of(new SimpleGrantedAuthority("ROLE_LABORATORY"));
        } else if (this.role == RoleEnum.PATIENT) {
            return List.of(new SimpleGrantedAuthority("ROLE_PATIENT"));
        } else return List.of(new SimpleGrantedAuthority("ROLE_SECRETARY"));

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
