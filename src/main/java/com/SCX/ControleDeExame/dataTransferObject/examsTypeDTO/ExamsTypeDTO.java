package com.SCX.ControleDeExame.dataTransferObject.examsTypeDTO;

import com.SCX.ControleDeExame.domain.exams.Exams;
import com.SCX.ControleDeExame.domain.examsType.ExamsType;

public record ExamsTypeDTO(String name) {

    public ExamsTypeDTO(ExamsType examsType){
        this(examsType.getName());
    }

}
