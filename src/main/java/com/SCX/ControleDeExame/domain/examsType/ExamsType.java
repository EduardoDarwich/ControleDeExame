package com.SCX.ControleDeExame.domain.examsType;

import com.SCX.ControleDeExame.domain.exams.Exams;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "exams_type")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class ExamsType {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;




}
