package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.*;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.infra.security.TokenService;
import com.SCX.ControleDeExame.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {

        try {
            var passUser = new UsernamePasswordAuthenticationToken(data.usernameKey(), data.password_key());
            var auth = this.authenticationManager.authenticate(passUser);
            var token = tokenService.generateToken((Auth) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

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

    @PostMapping("/verificUserExists")
    public ResponseEntity verificUserExists(@RequestBody @Valid AuthVerificDTO data) {
        boolean result = authService.authVerific(data);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/verificUserActive")
    public ResponseEntity verificUserActive(@RequestBody @Valid AuthVerificDTO data){
        boolean result = authService.verificUserActive(data);
        return ResponseEntity.ok(result);
    }


}
