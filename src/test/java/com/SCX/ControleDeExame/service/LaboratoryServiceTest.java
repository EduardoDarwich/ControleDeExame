package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.ResponseClinicLabDTO;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import com.SCX.ControleDeExame.repository.*;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
class LaboratoryServiceTest {

    @Autowired
    ClinicRepository clinicRepository;

    @Autowired
    LaboratoryService laboratoryService;

    @Autowired
    LaboratoryRepository laboratoryRepository;

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    AuthRepository authRepository;



    @Test
    void clinicsLaboratory() {
        Clinic newClinic = new Clinic();
        newClinic.setName("SunSet");
        newClinic.setCnpj("53351115");
        newClinic.setTelephone("1234124");
        newClinic.setActive(true);
        clinicRepository.save(newClinic);

        Laboratory newLaboratory = new Laboratory();
        newLaboratory.setName("asdasdasdas");
        newLaboratory.setCnpj("12312331");
        newLaboratory.setTelephone("14124351");
        newLaboratory.setActive(true);
        laboratoryRepository.save(newLaboratory);

        newClinic.getLaboratories().add(newLaboratory);
        clinicRepository.save(newClinic);

        List <ResponseClinicLabDTO> data = laboratoryRepository.findClinicByLaboratory(newLaboratory.getId());

        if(data.isEmpty()){
            fail("falhou");
            return;
        }

        assertTrue(true);
    }

}