package com.SCX.ControleDeExame.infra.security;

import com.SCX.ControleDeExame.domain.auth.Auth;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {



    @Value("${api.security.token.secret}")
    private String security;
    public String generateToken(Auth auth){
        try {
            Algorithm algorithm = Algorithm.HMAC256(security);
            String token = JWT.create().withIssuer("api.scx")
                    .withSubject(auth.getId().toString())
                    .withExpiresAt(genExpirationDate()).sign(algorithm);
            return token;
        } catch (JWTCreationException exception){

            throw new RuntimeException("erro ao gerar o token" , exception);

        }
    }

    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public String registerUser (String token){
        try {
            System.out.println(token);
            Algorithm algorithm = Algorithm.HMAC256(security);
            return JWT.require(algorithm).withIssuer("api.scx").build().verify(token).getSubject();
        } catch (JWTVerificationException exception){
            return "";

        }
    }
}
