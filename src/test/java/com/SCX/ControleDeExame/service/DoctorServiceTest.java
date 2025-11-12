package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.role.Role;
import com.SCX.ControleDeExame.repository.AuthRepository;
import com.SCX.ControleDeExame.repository.ClinicRepository;
import com.SCX.ControleDeExame.repository.DoctorRepository;
import com.SCX.ControleDeExame.repository.RoleRepository;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class DoctorServiceTest {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    AuthRepository authRepository;
    @Autowired
    ClinicRepository clinicRepository;

    @Test
    @DisplayName("Retorno com sucesso")
    void verificDocCli() {
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

        boolean b  = clinicRepository.existsDoctorClinic(newClinic.getId(), newDoctor.getId());
        assertThat(b).isTrue();

    }

    @Test
    void clinicDocActive() {
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



        if (newDoctor.getIdClinic().equals(newClinic.getId()) ){
            assertTrue(true);
            return;

        }
        fail("reprovou");



    }
}