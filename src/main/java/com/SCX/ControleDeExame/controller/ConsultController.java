package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.cnpjVerifyDTO.CnpjVerifyDTO;
import com.SCX.ControleDeExame.dataTransferObject.cnpjVerifyDTO.GetCnpjDTO;
import com.SCX.ControleDeExame.dataTransferObject.viaCepDTO.RequestCEPDTO;
import com.SCX.ControleDeExame.dataTransferObject.viaCepDTO.ViaCepDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/consult")
public class ConsultController {

    @PostMapping("/getCep")
    public ViaCepDTO consultCep (@RequestBody @Valid RequestCEPDTO data){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ViaCepDTO> resp = restTemplate.getForEntity(String.format("https://viacep.com.br/ws/%s/json", data.cep()), ViaCepDTO.class);
        return resp.getBody();

    }

    @PostMapping("/getCnpj")
    public CnpjVerifyDTO buscarCnpj(@RequestBody @Valid GetCnpjDTO data) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CnpjVerifyDTO> resp = restTemplate.getForEntity(String.format("https://www.receitaws.com.br/v1/cnpj/" + data.cnpj()), CnpjVerifyDTO.class);

        return resp.getBody();
    }
}
