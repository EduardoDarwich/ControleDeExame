package com.SCX.ControleDeExame.dataTransferObject.prontuarioDTO;

import com.SCX.ControleDeExame.dataTransferObject.anamnesisDTO.CreateCustomFieldDTO;

import java.util.List;

public record ResponseAnamnesisDTO(
        String mainComplaint,
        String historyOfCurrentIllness,
        String personalMedicalHistory,
        String familyHistory,
        String allergies,
        String useMedications,
        String previousHospitalizations,
        String previousSurgeries,
        String diet,
        String sleep,
        String physicalActivity,
        boolean smoking,
        boolean alcoholism,
        String bloodPressure,
        String heartRate,
        double temperature,
        double weight,
        double height,
        double bmi,
        String observations,
        String diagnosticHypothesis,
        String treatmentPlan,
        List<CreateCustomFieldDTO> customFields
) {
}
