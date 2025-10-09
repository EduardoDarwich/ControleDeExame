package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.FirstLoginTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.authDTO.FistLoginPasswordDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.repository.AuthRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;


//Classe contendo a logica para fazer a procura no banco de dados
@Service
public class AuthService implements UserDetailsService {

//Instância automatica da interface AuthRepository
    @Autowired
    AuthRepository authRepository;

//Metodo do Spring security para realizar a consulta do usuario
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authRepository.findByUsernameKey(username);
    }

    public void deletAuth (UUID uuid){
        Auth auth = authRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
        authRepository.delete(auth);
    }

    public void firstLogin(FistLoginPasswordDTO data, FirstLoginTokenDTO dataT){

            Auth auth = authRepository.findByToken(dataT.token());
            if (auth.getToken_status() && auth.getData_expiration_token().before(Timestamp.valueOf(LocalDateTime.now())) == false){
                String encryptedPassword = new BCryptPasswordEncoder().encode(data.password_key());
                auth.setPassword_key(encryptedPassword);
                auth.setStatus(true);
                auth.setToken_status(false);

                authRepository.save(auth);
            } else {
                System.out.println("deu erro");
            }

    }
}
