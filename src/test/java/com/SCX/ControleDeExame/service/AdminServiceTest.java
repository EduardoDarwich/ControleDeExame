package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseDocCliDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.RequestSecretaryEmailDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.role.Role;
import com.SCX.ControleDeExame.domain.secretary.Secretary;
import com.SCX.ControleDeExame.repository.*;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
class AdminServiceTest {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    AuthRepository authRepository;
    @Autowired
    ClinicRepository clinicRepository;
    @Autowired
    SecretaryRepository secretaryRepository;
    @Autowired
    AdminService adminService;

    @Test
    void docCli() {
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

        List<ResponseDocCliDTO> data = clinicRepository.findDocByClinic(newClinic.getId());


        if (data.isEmpty()) {
            fail("falhou");
            return;
        }

        assertTrue(true);

    }

    @Test
    void disableSecretary() {

        Auth newAuth = new Auth();
        Secretary newSecretary = new Secretary();
        Role secretary = roleRepository.findByName("Secretary");

        newAuth.setPassword_key("123456789");
        newAuth.setUsernameKey("medic123o@gmail.com");
        newAuth.setName("lina");
        newAuth.setActive(true);
        newAuth.getRoles().add(secretary);
        authRepository.save(newAuth);

        Clinic newClinic = new Clinic();
        newClinic.setName("SunSet123");
        newClinic.setCnpj("531235");
        newClinic.setTelephone("123213124");
        newClinic.setActive(true);
        clinicRepository.save(newClinic);

        newSecretary.setCpf("1223123");
        newSecretary.setAuthId(newAuth);
        newSecretary.setClinicId(newClinic);
        newSecretary.setTelephone("2321123232");
        secretaryRepository.save(newSecretary);

        RequestSecretaryEmailDTO requestSecretaryEmailDTO = new RequestSecretaryEmailDTO(newSecretary.getAuthId().getUsernameKey());

        adminService.disableSecretary(requestSecretaryEmailDTO);
        System.out.println(newSecretary.getAuthId().getActive());
        Secretary updatedSecretary = secretaryRepository.findById(newSecretary.getId()).get();


        if (updatedSecretary.getAuthId().getActive() == false){
            assertTrue(true);
            return;
        }

        fail("reprovou");


    }
}