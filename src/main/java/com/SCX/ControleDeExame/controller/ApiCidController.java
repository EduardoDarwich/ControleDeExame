package com.SCX.ControleDeExame.controller;

import com.SCX.ControleDeExame.dataTransferObject.apiCidDTO.ApiCidDTO;
import com.SCX.ControleDeExame.dataTransferObject.apiCidDTO.ReturnCidAPIDTO;
import com.SCX.ControleDeExame.dataTransferObject.viaCepDTO.RequestCEPDTO;
import com.SCX.ControleDeExame.service.APICidService;
import jakarta.validation.Valid;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.LinkedList;

@RestController
@RequestMapping("/API_CID")
public class ApiCidController {

    @Autowired
    APICidService apiCidService;

    @PostMapping("/getToken")
    public ResponseEntity getToken() throws Exception {
        return ResponseEntity.ok(apiCidService.getToken());
    }

    @PostMapping("/requestAPI")
    public ResponseEntity<LinkedList<ReturnCidAPIDTO>> requestAPI(@RequestBody ApiCidDTO data) throws Exception {

        return ResponseEntity.ok(apiCidService.search( data.disease()));
    }
}
