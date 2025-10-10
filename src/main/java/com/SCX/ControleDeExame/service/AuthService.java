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
    //Metodo para deletar um usuario
    public void deletAuth (UUID uuid){
        //Criando uma instancia de usuario com o usuario buscado pelo id informado
        Auth auth = authRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
        //Deletando o usuario encontrado
        authRepository.delete(auth);
    }
    //Metodo de primeiro login
    public void firstLogin(FistLoginPasswordDTO data, FirstLoginTokenDTO dataT){
            //Criando instância de um usuario buscado token recebido
            Auth auth = authRepository.findByToken(dataT.token());
            //Validando se o token é valido e não está expirado
            if (auth.getToken_status() && auth.getData_expiration_token().before(Timestamp.valueOf(LocalDateTime.now())) == false){
                //Criptografando a nova senha passada pelo usuario
                String encryptedPassword = new BCryptPasswordEncoder().encode(data.password_key());
                //Setando nova senha
                auth.setPassword_key(encryptedPassword);
                //Alterando o status do usuario para true ou seja ativo
                auth.setStatus(true);
                //Alterando o status do token para falso impossibilitanodo que o mesmo token seja usado de novo
                auth.setToken_status(false);
                //Salvando os novos dados do usuário no banco de dados
                authRepository.save(auth);
            } else {
                //Mensagem temporaria de erro
                System.out.println("deu erro");
            }

    }
}
