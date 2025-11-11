package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.adminSystemDTO.ResponseCliSystDTO;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.repository.ClinicRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AdminSystemServiceTest {

    @Autowired
    ClinicRepository clinicRepository;

    @Autowired
    AdminSystemService adminSystemService;


    @Test
    void listAllClinics() {
        Clinic newClinic = new Clinic();
        newClinic.setName("SunSet");
        newClinic.setCnpj("53351115");
        newClinic.setTelephone("1234124");
        newClinic.setActive(true);
        clinicRepository.save(newClinic);

        List<ResponseCliSystDTO> data = adminSystemService.listAllClinics();

        if (data.isEmpty()) {
            fail("falhou");
            return;
        }

        assertTrue(true);



    }

    @Test
    void countLabs() {

       long count = adminSystemService.countLabs();

       if (count == 0) {
           assertTrue(true);
           return;
       }
        fail("falhou");
    }
}