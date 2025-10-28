package com.SCX.ControleDeExame.domain.address;

import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "address")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Address {
    @Id
    @GeneratedValue
    private UUID id;
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade;
    private String uf;

    @OneToOne(mappedBy = "address", fetch = FetchType.EAGER)
    private Clinic clinic;

    @OneToOne(mappedBy = "address", fetch = FetchType.EAGER)
    private Laboratory laboratory;
}
