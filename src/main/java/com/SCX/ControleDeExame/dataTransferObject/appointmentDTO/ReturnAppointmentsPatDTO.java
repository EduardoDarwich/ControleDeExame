package com.SCX.ControleDeExame.dataTransferObject.appointmentDTO;

import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReturnAppointmentsPatDTO(String nameM, String specialty, LocalDateTime dateEnd, String nameC, UUID idAppointment) {
}
