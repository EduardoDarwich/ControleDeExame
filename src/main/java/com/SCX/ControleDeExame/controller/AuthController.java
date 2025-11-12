package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.adminSystemDTO.UpdateAdminDTO;
import com.SCX.ControleDeExame.dataTransferObject.authDTO.*;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.UpdateDocDTO;
import com.SCX.ControleDeExame.dataTransferObject.logDTO.HistoryDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.UpdatePatDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.UpdateSecretaryDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.infra.security.TokenService;
import com.SCX.ControleDeExame.service.AuthService;
import com.SCX.ControleDeExame.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;

    @Autowired
    AuthService authService;

    @Autowired
    LogService logService;

    //Rota de login
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {

        try {
            var passUser = new UsernamePasswordAuthenticationToken(data.usernameKey(), data.password_key());
            var auth = this.authenticationManager.authenticate(passUser);
            Auth auth1 = (Auth) auth.getPrincipal();

            if (!auth1.getActive()){
               return ResponseEntity.badRequest().build() ;
            }
            var token = tokenService.generateToken((Auth) auth.getPrincipal());



            return ResponseEntity.ok(new LoginResponseDTO(token));

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Rota de first login
    @PostMapping("/first-login/{token}")
    public ResponseEntity firstLogin(@RequestBody @Valid FistLoginPasswordDTO data, @PathVariable("token") @Valid FirstLoginTokenDTO dataT) {
        try {

            authService.firstLogin(data, dataT);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Rota para devolver o perfil do usuario
    @GetMapping("/perfil")
    public ResponseEntity perfil(@RequestHeader("Authorization") RequestTokenDTO dataT) {
        try {
            PerfilDTO perfil = authService.perfil(dataT);
            return ResponseEntity.ok(perfil);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Rota para verificar se o usuario existe no sistema
    @PostMapping("/verificUserExists")
    public ResponseEntity verificUserExists(@RequestBody @Valid AuthVerificDTO data) {
        boolean result = authService.authVerific(data);
        return ResponseEntity.ok(result);
    }

    //Rota para verificar se o usuario está ativo
    @PostMapping("/verificUserActive")
    public ResponseEntity verificUserActive(@RequestBody @Valid AuthVerificDTO data){
        boolean result = authService.verificUserActive(data);
        return ResponseEntity.ok(result);
    }

    //Rota para retornar o historico do usuario
    @GetMapping("/getHistory")
    public ResponseEntity<List<HistoryDTO>> getHistory(@RequestHeader("Authorization") RequestTokenDTO dataT){
        return ResponseEntity.ok(logService.getHistory(dataT));
    }

    //Rota para retornar os dados do Medico
    @GetMapping("/getProfileDoctor")
    public ResponseEntity getProfileDoctor (@RequestHeader("Authorization") RequestTokenDTO dataT){
        return ResponseEntity.ok(authService.profileDoc(dataT));
    }

    //Rota para retornar os dados do Paciente
    @GetMapping("/getProfilePatient")
    public ResponseEntity getProfilePatient (@RequestHeader("Authorization") RequestTokenDTO dataT){
        return ResponseEntity.ok(authService.profilePatient(dataT));
    }

    //Rota para retornar os dados da secretaria
    @GetMapping("/getProfileSecretary")
    public ResponseEntity getProfileSecretary (@RequestHeader("Authorization") RequestTokenDTO dataT){
        return ResponseEntity.ok(authService.profileSecretary(dataT));
    }

    //Rota para retornar os dados do Admin
    @GetMapping("/getProfileAdmin")
    public ResponseEntity getProfileAdmin (@RequestHeader("Authorization") RequestTokenDTO dataT){
        return ResponseEntity.ok(authService.profileAdmin(dataT));
    }

    //Rota para atualizar os dados do paciente
    @PatchMapping("/updatePat")
    public ResponseEntity updatePat (@RequestHeader("Authorization") RequestTokenDTO dataT, @RequestBody @Valid UpdatePatDTO data){
        authService.updatePaciente(dataT, data);
        return ResponseEntity.ok().build();
    }

    //Rota para atualizar os dados do medico
    @PatchMapping("/updateDoc")
    public ResponseEntity updateDoc (@RequestHeader("Authorization") RequestTokenDTO dataT, @RequestBody @Valid UpdateDocDTO data){
        authService.updateDoctor(dataT, data);
        return ResponseEntity.ok().build();
    }

    //Rota para atualizar os dados do secretary
    @PatchMapping("/updateSecretary")
    public ResponseEntity updateSecretary (@RequestHeader("Authorization") RequestTokenDTO dataT, @RequestBody @Valid UpdateSecretaryDTO data){
        authService.updateSecretary(dataT, data);
        return ResponseEntity.ok().build();
    }

    //Rota para atualizar os dados do admin
    @PatchMapping("/updateAdmin")
    public ResponseEntity updateAdmin (@RequestHeader("Authorization") RequestTokenDTO dataT,@RequestBody @Valid UpdateAdminDTO data){
        authService.updateAdmin(dataT, data);
        return ResponseEntity.ok().build();
    }


}
