package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.FirstLoginTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.authDTO.FistLoginPasswordDTO;
import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.LaboratoryVerificDTO;
import com.SCX.ControleDeExame.service.ResetPasswordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/resetPassword")
public class ResetPasswordController {

    @Autowired
    ResetPasswordService resetPasswordService;

    //Rota gerar o token de resetPassword
    @PatchMapping("/generateToken")
    public ResponseEntity generateTokenReset (RequestTokenDTO dataT){
        resetPasswordService.generateResetToken(dataT);
        return ResponseEntity.ok().build();
    }

    //Rota para Fazer o reset de senha
    @PostMapping("/resetPassword/{token}")
    public ResponseEntity firstLogin(@RequestBody @Valid FistLoginPasswordDTO data, @PathVariable("token") @Valid FirstLoginTokenDTO dataT){
        resetPasswordService.resetPassword(data, dataT);
        return ResponseEntity.ok().build();
    }
}
