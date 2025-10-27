package com.SCX.ControleDeExame.dataTransferObject.logDTO;

import com.SCX.ControleDeExame.domain.log.Log;
import com.SCX.ControleDeExame.domain.patient.Patient;

import java.util.UUID;

public record LogDTO( String userAction, UUID entityId, String old_value, String new_value, String hour_event, String status) {




}
