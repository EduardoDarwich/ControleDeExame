package com.SCX.ControleDeExame.dataTransferObject.examsDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetByDoctorDTO {

    private String cid;
    private String status ;
    private String result_value;
    private String result_filter_url;
    private String observation;
    private String laboratory;
    private String patient;



}
