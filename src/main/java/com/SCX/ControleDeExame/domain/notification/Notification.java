package com.SCX.ControleDeExame.domain.notification;

import com.SCX.ControleDeExame.domain.auth.Auth;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "notification")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Notification {

    @Id
    @GeneratedValue
    private UUID id;
    private String title;
    private String message;
    @Column(name = "read_file")
    private boolean readFile;

    @ManyToOne()
    @JoinColumn(name = "auth_id", nullable = false, unique = true)
    private Auth authId;

}
