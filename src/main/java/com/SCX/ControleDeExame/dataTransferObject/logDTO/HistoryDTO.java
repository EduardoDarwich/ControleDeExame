package com.SCX.ControleDeExame.dataTransferObject.logDTO;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record HistoryDTO(String name, String userACT, @JsonFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime dateTime) {
}
