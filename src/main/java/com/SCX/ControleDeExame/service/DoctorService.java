package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.DoctorDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsDTO.GetByDoctorDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsRequestDTO.ExamsRequestDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.exams.Exams;
import com.SCX.ControleDeExame.domain.examsRequest.ExamsRequest;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.domain.patient.Patient;
import com.SCX.ControleDeExame.domain.roleEnum.RoleEnum;
import com.SCX.ControleDeExame.infra.security.TokenService;
import com.SCX.ControleDeExame.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
//Classe contendo a logica da entidade do médico
@Service
public class DoctorService {
    //Criando instâncias das classes utilizadas
    @Autowired
    AuthRepository authRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    TokenService tokenService;

    @Autowired
    RequestExamsRepository requestExamsRepository;

    @Autowired
    ExamsRepository examsRepository;

    @Autowired
    LaboratoryRepository laboratoryRepository;

    @Autowired
    PatientRepository patientRepository;
    //Metodo para registrar o medico
    public Doctor registerDoctor(DoctorDTO data) {
        //Definindo uma senha temporaria aleatoria
        String senhaTemp = UUID.randomUUID().toString().substring(0, 8);
        //Definindo um token de autenticação aleatorio
        String token = UUID.randomUUID().toString();
        //Definindo o status inicial do medico como usuario
        Boolean status = false;
        //Definindo a data de expiração para o token de autenticação
        Timestamp expirationToken = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
        //Definindo o status do token de autenticação como ativo
        Boolean token_status = true;
        //Cripitografando a senha temporaria do usuario
        String encryptedPassword = new BCryptPasswordEncoder().encode(senhaTemp);
        //Criando uma instância de usuario com os dados recebidos
        Auth newAuth = new Auth(data.crm(), encryptedPassword, RoleEnum.DOCTOR, token, status, expirationToken, token_status);
        //Salvando esses dados no banco de dados
        authRepository.save(newAuth);

        //Try catch
        try {
            //Criando uma instância de médico com os dados recebidos
            Doctor newDoctor = new Doctor();
            newDoctor.setCrm(data.crm());
            newDoctor.setName(data.name());
            newDoctor.setEmail(data.email());
            newDoctor.setAuthId(newAuth);
            //Salvando os dados na tabela de médico no banco de dados
            return doctorRepository.save(newDoctor);

        } catch (Exception e) {
            //Caso tenha erro ao salvar os dados na tabela do médico deleta o usuario criado
            authRepository.delete(newAuth);
            e.printStackTrace();
            throw e;
        }

    }
    //Metodo para deletar um medico do banco de dados
    public void deleteDoctor(UUID uuid) {
        //Salvando os dados de um usuario buscado por id em uma instância de médico
        Doctor doctor = doctorRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("paciente não encontrado"));
        //Deletando esse médico do banco de dados
        doctorRepository.delete(doctor);

    }
    //Metodo para atualziar os dados de um médico no banco de dados
    public Doctor updateDoctor(DoctorDTO data, UUID uuid) {
        //Criando uma instância de médico com os dados vindos do banco de dados buscados pelo id passado
        Doctor doctorUpdate = doctorRepository.findById(uuid).orElseThrow(() -> new EntityNotFoundException("paciente não encontrado"));
        //Atulizando os dados
        doctorUpdate.setTelephone(data.telephone());
        //Salvando os dados atualizados desse médico
        return doctorRepository.save(doctorUpdate);

    }

    //Metodo para Listar os exames vinculados a um médico
    public List<GetByDoctorDTO> getExamsByDoctor(RequestTokenDTO data) {
        //Armazenando o token como String vindo do Front end e formatando ele pro formato correto recebbido pelo metodo
        var idC = data.toString().replace("RequestTokenDTO[Token=", "" ).replace("]", "");
        //Recuperando o id que está dentro do token
        var id = tokenService.registerUser(idC);
        //Criando uma instância de médico com os dados do médico que tem o id recebido pelo token
        Doctor doctorId = doctorRepository.findByAuthId_Id(UUID.fromString(id));
        //Devolvendo os exames associados a esse médico
        return examsRepository.findAllByDoctorId(doctorId.getId());
    }
    //Metodo para fazer a requisição dos exames
    public void requestExams(ExamsRequestDTO data, RequestTokenDTO dataT) {
        //Try catch
        try {
            //Armazenando a data atual em uma variável
            LocalDateTime now = LocalDateTime.now();
            //Criando uma formatação para a data
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            //Armazenando a data formatada em uma variável
            String dataToString = now.format(formatter);
            //Armazenando o token recebido e formatando para a forma correta para passar pro metodo
            var idC = dataT.toString().replace("RequestTokenDTO[Token=", "" ).replace("]", "");
            //Armazenando o Id do token em uma variável
            var id = tokenService.registerUser(idC);
            //Criando uma intância do médico ligao ao Id recebido dentro do token
            Doctor doctor = doctorRepository.findByAuthId_Id(UUID.fromString(id));
            //Criando uma intância da requisição de exames com os dados recebidos
            ExamsRequest newExamRequest = new ExamsRequest();
            newExamRequest.setDoctorId(doctor);
            newExamRequest.setExam_type(data.exam_type());
            newExamRequest.setSample_type(data.sample_type());
            newExamRequest.setComplement(data.complement());
            newExamRequest.setRequestDate(dataToString);
            //Salvando os dados da requisição de exames no banco de dados
            requestExamsRepository.save(newExamRequest);
            //Armazenando Cpf e cnpj do paciente e laboratorio recebidos
            String cnpj = data.cnpj();
            String cpf = data.cpf();
            //Criando instâncias dessas duas entidades associadas aos cpfs e cnpj
            Laboratory laboratory = laboratoryRepository.findByCnpj(cnpj);
            Patient patient = patientRepository.findByCpf(cpf);

            //Criando um status inicial para um exame após a sua requisição ser feita
            Exams newExam = new Exams();
            newExam.setStatus("Pendente");
            newExam.setDoctor(doctor);
            newExam.setRequestId(newExamRequest);
            newExam.setLaboratoryId(laboratory);
            newExam.setPatientId(patient);
            //Salvando esses status iniciais de exame no banco de dados
            examsRepository.save(newExam);


        } catch (Exception e) {
            e.printStackTrace();
            throw e;

        }
    }
    //Metodo para procurar exames por status
    public void getByExamsStatus() {
    }
    //Metodo para buscar exames associados a um paciente
    public void getByPatientName() {
    }
}
