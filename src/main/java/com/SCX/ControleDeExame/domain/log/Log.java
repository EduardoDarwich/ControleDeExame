package com.SCX.ControleDeExame.domain.log;

import com.SCX.ControleDeExame.domain.auth.Auth;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "log")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@Setter
public class Log {
    @Id
    @GeneratedValue
    private UUID id;
    private String userAction;
    private String old_value;
    private String new_value;
    private LocalDateTime hour_event;
    private String status;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "auth_id", nullable = false, unique = true)
    private Auth authId;


}
