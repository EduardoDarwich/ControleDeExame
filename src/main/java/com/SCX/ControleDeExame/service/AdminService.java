package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.adminDTO.AdminDTO;
import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.LaboratoryDTO;
import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.GetLaboratoryCNPJDTO;
import com.SCX.ControleDeExame.dataTransferObject.logDTO.LogDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.RequestSecretaryEmailDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.SecretaryDTO;
import com.SCX.ControleDeExame.domain.admin.Admin;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.domain.roleEnum.RoleEnum;
import com.SCX.ControleDeExame.domain.secretary.Secretary;
import com.SCX.ControleDeExame.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

//@Service indica pro spring que essa class é uma Service
@Service
public class AdminService {

    //@Autowired cria uma instância de uma certa class
    @Autowired
    AdminRepository adminRepository;

    @Autowired
    SecretaryRepository secretaryRepository;

    @Autowired
    AuthRepository authRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    LaboratoryRepository laboratoryRepository;

    @Autowired
    LogRepository logRepository;

    public Admin registeAdmin (AdminDTO data){

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password_key());
        Auth newAuth = new Auth();
        newAuth.setPassword_key(encryptedPassword);
        newAuth.setUsernameKey(data.cpf());
        newAuth.setRole(RoleEnum.ADMIN);
        authRepository.save(newAuth);

        try{
        Admin newAdmin = new Admin();
        newAdmin.setName(data.name());
        newAdmin.setEmail(data.email());
        newAdmin.setTelephone(data.telephone());
        newAdmin.setCpf(data.cpf());
        newAdmin.setAuth_id(newAuth);

        //emailService.sendEmail("felipegomes007goga@gmail.com", "Novo usuario cadastrado", "Voce foi cadastrado com sucesso");


        return adminRepository.save(newAdmin);
        } catch (Exception e) {
            authRepository.delete(newAuth);
            e.printStackTrace();
            throw e;
        }


    }

    //Metodo para cadastrar um usuário da secretaria
    public Secretary registerSecretary(SecretaryDTO data) {

        //Definindo a senha temporária de primeiro login
        String senhaTemp = UUID.randomUUID().toString().substring(0, 8);
        //Definindo o Token de ativação
        String token = UUID.randomUUID().toString();
        //Definindo Status padrões do usuário antes do primeiro login
        Boolean status = false;
        Timestamp expirationToken = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
        Boolean token_status = true;
        //Criptografando a senha temporária
        String encryptedPassword = new BCryptPasswordEncoder().encode(senhaTemp);
        //Criando uma instância de usuário com os dados definidos
        Auth newAuth = new Auth(data.cpf(), encryptedPassword, RoleEnum.SECRETARY, token, status, expirationToken, token_status);
        //Salvando os dados da instância criada no banco de dados
        authRepository.save(newAuth);

        //Try catch para capturar possiveis erros
        try {
            //Criando instância de secretaria com os dados recebidos
            Secretary newSecretary = new Secretary();
            newSecretary.setCpf(data.cpf());
            newSecretary.setName(data.name());
            newSecretary.setEmail(data.email());
            newSecretary.setAuthId(newAuth);
            //Salvando os dados da instância criada no banco de dados
            return secretaryRepository.save(newSecretary);

        } catch (Exception e) {
            //Caso a tentativa de salvar os dados no banco falhe ele apaga o usuario criado para evitar usuarios sem ninguem associado
            authRepository.delete(newAuth);
            e.printStackTrace();
            throw e;
        }

    }

    //Metodo para cadastrar um usuário do laboratório
    public Laboratory registerLaboratory(LaboratoryDTO data) {

        //Definindo a senha temporária de primeiro login
        String senhaTemp = UUID.randomUUID().toString().substring(0, 8);
        //Definindo o Token de ativação
        String token = UUID.randomUUID().toString();
        //Definindo Status padrões do usuário antes do primeiro login
        Boolean status = false;
        Timestamp expirationToken = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
        Boolean token_status = true;
        //Criptografando a senha temporária
        String encryptedPassword = new BCryptPasswordEncoder().encode(senhaTemp);
        //Criando uma instância de usuário com os dados definidos
        Auth newAuth = new Auth(data.cnpj(), encryptedPassword, RoleEnum.SECRETARY, token, status, expirationToken, token_status);
        //Salvando os dados da instância criada no banco de dados
        authRepository.save(newAuth);

        //Try catch para capturar possiveis erros
        try {
            //Criando instância de laboratorio com os dados recebidos
            Laboratory newLaboratory = new Laboratory();
            newLaboratory.setName(data.name());
            newLaboratory.setEmail(data.email());
            newLaboratory.setAddress(data.address());
            newLaboratory.setTelephone(data.telephone());
            newLaboratory.setAuthId(newAuth);
            //Salvando os dados da instância criada no banco de dados
            return laboratoryRepository.save(newLaboratory);

        } catch (Exception e) {
            //Caso a tentativa de salvar os dados no banco falhe ele apaga o usuario criado para evitar usuarios sem ninguem associado
            authRepository.delete(newAuth);
            e.printStackTrace();
            throw e;
        }

    }

    //Metodo para atualizar dados de um usuário da secretaria
    public Secretary updateSecretary(SecretaryDTO data, UUID uuid) {
        //Criando uma instância de secretaria com os dados recebidos do banco de dados
        Secretary secretaryUpdate = secretaryRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
        //Atualizando os campos nescessários
        secretaryUpdate.setTelephone(data.telephone());
        secretaryUpdate.setSector(data.sector());
        //salvando a atualização no banco de dados
        return secretaryRepository.save(secretaryUpdate);

    }

    //Metodo para desabilitar um usuario da secretaria sem precisar deletar sua conta
    public void disableSecretary(RequestSecretaryEmailDTO data) {
        //Salvando os dados recebidos na variável email
        String email = data.Email();
        //Realizando a busca da instância de secretaria por esse email e salvando essa instância na variavel
        Secretary secretary = secretaryRepository.findByEmail(email);
        //Realizando a busca do usuário pelo id da instância de secertaria e savando essa instância na variável
        Auth auth = authRepository.findById(secretary.getAuthId().getId()).orElseThrow();
        //Realizando as alterações no status do usuário
        auth.setStatus(false);

    }

    //Metodo para desabilitar um usuário do laboratório sem precisar deletar sua conta
    public void disableLaboratory(GetLaboratoryCNPJDTO data) {
        //Salvando os dados recebidos na variável cnpj
        String cnpj = data.cnpj();
        //Realizando a busca da instância de laboratório por esse cnpj e salvando essa instância na variavel
        Laboratory laboratory = laboratoryRepository.findByCnpj(cnpj);
        //Realizando a busca do usuário pelo id da instância de laboratório e savando essa instância na variável
        Auth auth = authRepository.findById(laboratory.getAuthId().getId()).orElseThrow();
        //Realizando alterações no status do usuário
        auth.setStatus(false);

    }

    //Metodo para habilitar um usuário do laboratório
    public void enableLaboratory(GetLaboratoryCNPJDTO data) {
        //Salvando os dados recebidos na variável cnpj
        String cnpj = data.cnpj();
        //Realizando a busca da innstância de laboratório por esse cnpj e salvando essa instância na variavel
        Laboratory laboratory = laboratoryRepository.findByCnpj(cnpj);
        //Realizando a busca do usuário pelo id da instância de laboratório e savando essa instância na variável
        Auth auth = authRepository.findById(laboratory.getAuthId().getId()).orElseThrow();
        //Realizando alterações no status do usuário
        auth.setStatus(true);

    }

    //Metodo para habilitar um usuário da secretaria
    public void enableSecretary(RequestSecretaryEmailDTO data) {
        //Salvando os dados recebidos na variável email
        String email = data.Email();
        //Realizando a busca da innstância de secretaria por esse cnpj e salvando essa instância na variavel
        Secretary secretary = secretaryRepository.findByEmail(email);
        //Realizando a busca do usuário pelo id da instância de secretaria e savando essa instância na variável
        Auth auth = authRepository.findById(secretary.getAuthId().getId()).orElseThrow();
        //Realizando alterações no status do usuário
        auth.setStatus(true);

    }

    public List<LogDTO> getAllLog (){

        return logRepository.findAll().stream().map(LogDTO::new).toList();
    }


}
