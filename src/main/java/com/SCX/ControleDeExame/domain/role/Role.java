package com.SCX.ControleDeExame.domain.role;

import com.SCX.ControleDeExame.domain.auth.Auth;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Table(name = "role")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Role {

    @Id
    @GeneratedValue
    private UUID id;
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Set<Auth> usuarios = new HashSet<>();

}
