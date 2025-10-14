/*package com.SCX.ControleDeExame.domain.user_cli;

import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table (name = "user_cli")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCli {
    @EmbeddedId
    private UserCliId id;

    @ManyToMany
    @MapsId("idUser")
    @JoinColumn(name = "id_user")
    private Auth auth;

    @ManyToMany
    @MapsId("idClinic")
    @JoinColumn(name = "id_clinic")
    private Clinic clinic;
}*/
