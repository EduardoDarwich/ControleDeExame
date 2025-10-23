package com.SCX.ControleDeExame.dataTransferObject.appointmentDTO;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


@Getter
public class GetAppointmentOpenDocDTO {
    private String name;
    private LocalTime localTime;

    public GetAppointmentOpenDocDTO(String name, LocalDateTime time){
        this.name = name;
        this.localTime = LocalTime.parse(time.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
    }

}
