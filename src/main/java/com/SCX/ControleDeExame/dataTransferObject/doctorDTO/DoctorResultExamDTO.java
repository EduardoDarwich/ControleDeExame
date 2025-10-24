package com.SCX.ControleDeExame.dataTransferObject.doctorDTO;

import lombok.Getter;

@Getter
public class DoctorResultExamDTO {
    private String cid;
    private String result_value;
    private String observation;

    public DoctorResultExamDTO (String cid, String result_value, String observation){
        this.cid = cid;
        this.result_value = result_value;
        this.observation = observation;
    }
}
