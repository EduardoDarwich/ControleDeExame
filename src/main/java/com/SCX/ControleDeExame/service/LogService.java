package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.logDTO.HistoryDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.log.Log;
import com.SCX.ControleDeExame.infra.security.TokenService;
import com.SCX.ControleDeExame.repository.AuthRepository;
import com.SCX.ControleDeExame.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class LogService {
    @Autowired
    LogRepository logRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    AuthRepository authRepository;

    //Metodo para criar um registro de historico (testar)
    public void logAction(Auth user, String action) {
        var history = new Log();
        history.setAuthId(user);
        history.setUserAction(action);
        history.setHour_event(LocalDateTime.now());
        logRepository.save(history);
    }

    //Metodo para o usuario ver seu histórico (testar)
    public List<HistoryDTO> getHistory(RequestTokenDTO dataT) {

        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        var auth = authRepository.findById(UUID.fromString(id));

        return logRepository.findAllByAuth(auth.get().getId());
    }


}
