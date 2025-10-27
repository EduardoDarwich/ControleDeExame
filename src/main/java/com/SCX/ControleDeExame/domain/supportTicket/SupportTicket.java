package com.SCX.ControleDeExame.domain.supportTicket;

import ch.qos.logback.classic.net.SimpleSSLSocketServer;
import com.SCX.ControleDeExame.domain.auth.Auth;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "support_ticket")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class SupportTicket {

    @Id
    @GeneratedValue
    private UUID id;
    private String subject;
    private String message;
    private String response;
    private boolean finished;
    @Column(name = "create_data")
    private LocalDate createData;
    @Column(name = "finished_data")
    private LocalDate finishedData;

    @ManyToOne
    @JoinColumn(name = "auth_id", nullable = false, unique = true)
    private Auth authId;



}
