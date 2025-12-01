package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.apiCidDTO.ReturnCidAPIDTO;
import com.SCX.ControleDeExame.domain.api.Api;
import com.SCX.ControleDeExame.repository.ApiRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class APICidService {

    @Autowired
    ApiRepository apiRepository;

    //Definindo os dados para conexão e autenticação da api
    private final String tokenEndpoint = "https://icdaccessmanagement.who.int/connect/token";
    @Value("${api.id.cid}")
    private String clientId;
    @Value("${api.secret.cid}")
    private String clientSecret ;
    private final String scope = "icdapi_access";
    private final String GRANT_TYPE = "client_credentials";

    private String getToken() throws Exception {

        //Criando conexão
        URL url = new URL(tokenEndpoint);
        HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        //Montando a string para com os dados para autenticação
        String urlParameters =
                "client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8) +
                "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8) +
                "&scope=" + URLEncoder.encode(scope, StandardCharsets.UTF_8) +
                "&grant_type=" + URLEncoder.encode(GRANT_TYPE, StandardCharsets.UTF_8);

        //Abrindo a saida de dados para poder enviar dados pelo body
        con.setDoOutput(true);
        //Criando um objeto para poder mandar dados pelo body
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        //Pegando o codigo da requisição ex: 200
        int responseCode = con.getResponseCode();

        //Criando uma leitura em buffer para ler as respostas devolvidas pelo servidor
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputInline;
        StringBuffer response = new StringBuffer();
        while ((inputInline = in.readLine()) != null) {
            response.append(inputInline);

        }

        in.close();

        JSONObject jsonObj = new JSONObject(response.toString());
        return jsonObj.getString("access_token");
    }

    public LinkedList<ReturnCidAPIDTO> search ( String url) throws Exception {

        String cleanUrl = url.trim().replaceAll("^'+|'+$", "");

        String urltT = "https://id.who.int/icd/release/11/2025-01/MMS/search?q=" + URLEncoder.encode(cleanUrl,StandardCharsets.UTF_8).replace("+", "%20") + "&subtreeFilterUsesFoundationDescendants=false&includeKeywordResult=false&useFlexisearch=false&flatResults=true&highlightingEnabled=true&medicalCodingMode=true";

        //Criando a conexão
        URL url1 = new URL(urltT);
        HttpURLConnection con = (HttpURLConnection) url1.openConnection();
        con.setRequestMethod("GET");

        Optional<Api> token = apiRepository.findById(UUID.fromString("548e1240-e5c3-4068-bf85-1f0a938a29ac"));



        //Configurando o header http
        con.setRequestProperty("Authorization", "Bearer " + token.get().getToken());
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Accept-Language", "pt-br");
        con.setRequestProperty("API-Version", "v2");

        if (con.getResponseCode() == 401){

            token.get().setToken(getToken());
            apiRepository.save(token.get());
        }

        //Pegando o codigo da requisição ex: 200
        int responseCode = con.getResponseCode();

        //Criando uma leitura em buffer para ler as respostas devolvidas pelo servidor
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputInline;
        StringBuffer response = new StringBuffer();
        while ((inputInline = in.readLine()) != null) {
            response.append(inputInline);

        }

        in.close();
        JSONObject jsonObj = new JSONObject(response.toString());
        JSONArray entities = jsonObj.getJSONArray("destinationEntities");

        LinkedList<ReturnCidAPIDTO> result = new LinkedList<>();

        for (int i = 0; i< entities.length(); i++){
            JSONObject entity = entities.getJSONObject(i);
            boolean contains = false;

            String titleRawClean = entity.getString("title").replaceAll("<[^>]*>", "").toLowerCase();
            String query = url.toLowerCase();


            if (titleRawClean.contains(query)){
                contains = true;
            }

            JSONArray pv = entity.getJSONArray("matchingPVs");
            for (int j = 0; j< pv.length(); j++){
                JSONObject match = pv.getJSONObject(j);
                String labelRawClean = match.getString("label").replaceAll("<[^>]*>", "").toLowerCase();
                if (labelRawClean.contains(query)){
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                continue;
            }
            String format = entity.getString("title").replaceAll("<[^>]*>", "").trim() + "= " + entity.getString("theCode")  ;

            String[] parts = format.split("=", 2);
            String descricao = parts[0].trim();
            String codigo = parts[1].trim();

            ReturnCidAPIDTO data = new ReturnCidAPIDTO(descricao, codigo);

            result.add(data);


        }




        return result;
    }


}
