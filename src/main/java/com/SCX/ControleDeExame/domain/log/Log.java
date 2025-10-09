package com.SCX.ControleDeExame.domain.log;

import jakarta.persistence.*;
import lombok.*;

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
    @Column(name = "entity_id")
    private UUID entityId;
    private String old_value;
    private String new_value;
    private String hour_event;
    private String status;

}
