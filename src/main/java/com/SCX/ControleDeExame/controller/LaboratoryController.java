package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.LaboratoryDTO;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.service.LaboratoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/laboratory")
public class LaboratoryController {

    @Autowired
    LaboratoryService laboratoryService;

    @PostMapping("/register")
    public ResponseEntity register (@RequestBody @Valid LaboratoryDTO data){
        Laboratory laboratory = laboratoryService.registerLaboratory(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(laboratory);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete (@PathVariable UUID id){
        laboratoryService.deleteLaboratory(id);
        return ResponseEntity.ok().build();
    }
}
