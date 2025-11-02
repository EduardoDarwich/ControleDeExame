package com.SCX.ControleDeExame.domain.anamnesis;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "anamnesis_custom")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class AnamnesisCustom {
    @Id
    @GeneratedValue
    private UUID id;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "anamnesis_id", nullable = false, unique = true)
    private Anamnesis anamnesis;
    @Column(name = "field_name")
    private String fieldName;
    @Column(name = "field_value")
    private String fieldValue;
}
