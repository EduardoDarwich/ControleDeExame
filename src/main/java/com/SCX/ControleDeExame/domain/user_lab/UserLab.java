package com.SCX.ControleDeExame.domain.user_lab;

import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_lab")
@Entity
public class UserLab {

    @EmbeddedId
    private UserLabId id;
    private String email;

    @ManyToOne
    @MapsId("idLaboratory")
    @JoinColumn(name = "id_laboratory")
    private Laboratory laboratoryId;

    @ManyToOne
    @MapsId("idUser")
    @JoinColumn(name = "id_user")
    private Auth authId;

}
