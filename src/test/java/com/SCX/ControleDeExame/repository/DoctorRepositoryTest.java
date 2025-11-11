package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.CreateDoctorDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.role.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class DoctorRepositoryTest {

    @Autowired
    EntityManager entityManager;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    AuthRepository authRepository;

    @Test
    @DisplayName("Retorno com sucesso")
    void findByCrmSuccess() {
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

        Optional<Doctor>  doctor1 = Optional.ofNullable(doctorRepository.findByCrm(newDoctor.getCrm()));

        assertThat(doctor1.isPresent()).isTrue();


    }



}