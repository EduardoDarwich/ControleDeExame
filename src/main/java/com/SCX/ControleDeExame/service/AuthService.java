package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.*;
import com.SCX.ControleDeExame.dataTransferObject.roleDTO.RoleDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.infra.security.TokenService;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


//Classe contendo a logica para fazer a procura no banco de dados
@Service
public class AuthService implements UserDetailsService {

    //Instância automatica da interface AuthRepository
    @Autowired
    AuthRepository authRepository;

    @Autowired
    TokenService tokenService;

    //Metodo do Spring security para realizar a consulta do usuario
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authRepository.findByUsernameKey(username);
    }

    public void deletAuth(UUID uuid) {

        Auth auth = authRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
        authRepository.delete(auth);
    }

    public void firstLogin(FistLoginPasswordDTO data, FirstLoginTokenDTO dataT) {

        Auth auth = authRepository.findByToken(dataT.token());

        if (auth.getToken_status() && !auth.getData_expiration_token().before(Timestamp.valueOf(LocalDateTime.now()))) {

            String encryptedPassword = new BCryptPasswordEncoder().encode(data.password_key());

            auth.setPassword_key(encryptedPassword);
            auth.setActive(true);
            auth.setToken_status(false);
            authRepository.save(auth);
        } else {
            System.out.println("deu erro");
        }

    }

    //Metodo para verificar se um usuário ja está cadastrado no sistema
    public Auth authVerific(AuthVerificDTO data) {
        try {
            Optional<Auth> userOPT = authRepository.findAuthByUsernameKey(data.email());
            return userOPT.get();

        } catch (Exception e) {

            e.printStackTrace();
            throw e;

        }
    }

    public PerfilDTO perfil (RequestTokenDTO dataT){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Auth auth = authRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
        List<RoleDTO> roles = auth.getRoles().stream().map(role -> new RoleDTO(role.getName())).collect(Collectors.toList());
        return new PerfilDTO(auth.getName(), roles);
    }
}

