package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.authDTO.*;
import com.SCX.ControleDeExame.dataTransferObject.profileDTO.ProfileAdminDTO;
import com.SCX.ControleDeExame.dataTransferObject.profileDTO.ProfileDoctorDTO;
import com.SCX.ControleDeExame.dataTransferObject.profileDTO.ProfilePatientDTO;
import com.SCX.ControleDeExame.dataTransferObject.profileDTO.ProfileSecretaryDTO;
import com.SCX.ControleDeExame.dataTransferObject.roleDTO.RoleDTO;
import com.SCX.ControleDeExame.domain.admin.Admin;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.patient.Patient;
import com.SCX.ControleDeExame.domain.secretary.Secretary;
import com.SCX.ControleDeExame.infra.security.TokenService;
import com.SCX.ControleDeExame.repository.*;
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

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    UserLabRepository userLabRepository;

    @Autowired
    ClinicRepository clinicRepository;

    @Autowired
    PatientRepository  patientRepository;

    @Autowired
    SecretaryRepository secretaryRepository;

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
    public boolean authVerific(AuthVerificDTO data) {

        Optional<Auth> userOPT = authRepository.findAuthByUsernameKey(data.email());
        if (userOPT.isPresent()) {
            return true;
        } else {
            return false;
        }

    }

    //Metodo para devolver os dados do Medico(testar)
    public ProfileDoctorDTO profileDoc (RequestTokenDTO dataT){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Doctor doctor = doctorRepository.findByAuthId_Id(UUID.fromString(id));

        return authRepository.findProfileDoctor(doctor.getId());
    }

    //Metodo para devolver os dados do Admin(testar)
    public ProfileAdminDTO profileAdmin (RequestTokenDTO dataT){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Admin admin = adminRepository.findByAuthId_Id(UUID.fromString(id));

        return authRepository.findProfileAdmin(admin.getId());
    }

    //Metodo para devolver os dados do paciente (testar)
    public ProfilePatientDTO profilePatient(RequestTokenDTO dataT){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Patient patient = patientRepository.findByAuthId_Id(UUID.fromString(id));

        return authRepository.findProfilePatient(patient.getId());
    }

    //Metodo para devolver os dados da secretaria (testar)
    public ProfileSecretaryDTO profileSecretary (RequestTokenDTO dataT){
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Secretary secretary = secretaryRepository.findByAuthId_Id(UUID.fromString(id));

        return authRepository.findProfileSecretary(secretary.getId());
    }


    //Metodo para devolver o perfil do usuario(nome, role e email)
    public PerfilDTO perfil(RequestTokenDTO dataT) {
        var idC = dataT.toString().replace("RequestTokenDTO[Token=Bearer ", "").replace("]", "");
        var id = tokenService.registerUser(idC);
        Auth auth = authRepository.findById(UUID.fromString(id)).orElseThrow(() -> new EntityNotFoundException("Usuario não encontrado"));
        List<RoleDTO> roles = auth.getRoles().stream().map(role -> new RoleDTO(role.getName())).collect(Collectors.toList());
        return new PerfilDTO(auth.getName(), roles, auth.getUsernameKey());
    }

    //Metodo para verificar se o usuario está ativo
    public boolean verificUserActive(AuthVerificDTO data) {
        Auth user = (Auth) authRepository.findByUsernameKey(data.email());
        return user.getActive();
    }


}

