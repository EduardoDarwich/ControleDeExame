package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.FirstLoginTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.authDTO.FistLoginPasswordDTO;
import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.infra.security.TokenService;
import com.SCX.ControleDeExame.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ResetPasswordService {

    @Autowired
    AuthRepository authRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    EmailService emailService;


    //Metodo para gerar o token de reset de senha (testar)
    public void generateResetToken (RequestTokenDTO dataT){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        String token = UUID.randomUUID().toString();
        Timestamp expirationToken = Timestamp.valueOf(LocalDateTime.now().plusMinutes(6));
        var auth = authRepository.findById(UUID.fromString(id));

        Auth auth1 = new Auth();
        auth1.setToken(token);
        auth1.setToken_status(true);
        auth1.setData_expiration_token(expirationToken);
        authRepository.save(auth1);
        //emailService.resetSenhaEmail(auth1);
    }

    //Metodo responsavel pelo reset de senha (testar)
    public void resetPassword (FistLoginPasswordDTO data, FirstLoginTokenDTO dataT){
        Auth auth = authRepository.findByToken(dataT.token());

        if (auth.getToken_status() && !auth.getData_expiration_token().before(Timestamp.valueOf(LocalDateTime.now()))) {

            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password_key());

            auth.setPassword_key(encryptedPassword);
            auth.setToken_status(false);
            authRepository.save(auth);
        } else {
            System.out.println("deu erro");
        }

    }

}
