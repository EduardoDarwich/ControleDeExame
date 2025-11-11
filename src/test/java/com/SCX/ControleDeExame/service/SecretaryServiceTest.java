package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseDocCliConsultDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.role.Role;
import com.SCX.ControleDeExame.repository.AuthRepository;
import com.SCX.ControleDeExame.repository.ClinicRepository;
import com.SCX.ControleDeExame.repository.DoctorRepository;
import com.SCX.ControleDeExame.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SecretaryServiceTest {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthRepository authRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    ClinicRepository clinicRepository;

    @Test
    void docCLiConsult() {
        Auth newAuth = new Auth();
        Doctor newDoctor = new Doctor();
        Role doctor = roleRepository.findByName("Doctor");

        newAuth.setPassword_key("123456789");
        newAuth.setUsernameKey("medico@gmail.com");
        newAuth.setName("lina");
        newAuth.setActive(true);
        newAuth.getRoles().add(doctor);
        authRepository.save(newAuth);


        //Cadastrando dados de médico ao usuario novo
        newDoctor.setCrm("12345");
        newDoctor.setAvailable(true);
        newDoctor.setAuthId(newAuth);
        newDoctor.setSpecialty("teste");

        doctorRepository.save(newDoctor);

        Clinic newClinic = new Clinic();
        newClinic.setName("SunSet");
        newClinic.setCnpj("53351115");
        newClinic.setTelephone("1234124");
        newClinic.getDoctors().add(newDoctor);
        newClinic.setActive(true);
        clinicRepository.save(newClinic);

        newDoctor.setIdClinic(newClinic.getId());
        doctorRepository.save(newDoctor);

        List<ResponseDocCliConsultDTO> data = clinicRepository.findDocConsultByClinic(newClinic.getId());

        if (data.isEmpty()){
            fail("falhou");
            return;
        }

        assertTrue(true);

    }
}