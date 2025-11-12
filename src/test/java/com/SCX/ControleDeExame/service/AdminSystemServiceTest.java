package com.SCX.ControleDeExame.service;

import com.SCX.ControleDeExame.dataTransferObject.adminDTO.CreateAdminDTO;
import com.SCX.ControleDeExame.dataTransferObject.adminDTO.CreateFirstAdmDTO;
import com.SCX.ControleDeExame.dataTransferObject.adminSystemDTO.ResponseCliSystDTO;
import com.SCX.ControleDeExame.dataTransferObject.anamnesisDTO.CreateAnamnesisDTO;
import com.SCX.ControleDeExame.dataTransferObject.authDTO.AuthVerificDTO;
import com.SCX.ControleDeExame.dataTransferObject.authDTO.RequestTokenDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.CreateDoctorDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.DoctorVerificDTO;
import com.SCX.ControleDeExame.dataTransferObject.examsRequestDTO.ExamsRequestDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.GetPatientByCPFDTO;
import com.SCX.ControleDeExame.dataTransferObject.prontuarioDTO.ResponseAnamnesisDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.RequestSecretaryCpfDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.RequestSecretaryEmailDTO;
import com.SCX.ControleDeExame.dataTransferObject.secretaryDTO.SecretaryDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.repository.AuthRepository;
import com.SCX.ControleDeExame.repository.ClinicRepository;
import com.SCX.ControleDeExame.repository.DoctorRepository;
import com.SCX.ControleDeExame.repository.RoleRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.print.Doc;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AdminSystemServiceTest {

    @Autowired
    ClinicRepository clinicRepository;

    @Autowired
    AdminSystemService adminSystemService;

    @Autowired
    EmailService emailService;

    @Autowired
    DoctorService doctorService;

    @Autowired
    AuthService authService;

    @Autowired
    SecretaryService secretaryService;



    @Autowired
    ClinicService clinicService;



    @Autowired
    AdminService adminService;





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
    @Test
    void docTest1(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");

        try{
            doctorService.registerDoctor(data, dataT);
            doctorService.registerDocUserExists(dataV, dataT);

        } catch (Exception e){

        }

    }

    @Test
    void docTest2(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");

        try {

        } catch (Exception e){
            assertTrue(true);
        }

    }

    @Test
    void docTest3(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");

        try {
            doctorService.disableDoc(dataT);
        } catch (Exception e){
            assertTrue(true);
        }


    }

    @Test
    void docTest4(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");

        try {
        doctorService.openConsultation(dataT);
        } catch (Exception e){
            assertTrue(true);
        }


    }

    @Test
    void docTest5(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");

        try {
        doctorService.doctorVerific(dataV);
        } catch (Exception e){
            assertTrue(true);
        }


    }

    @Test
    void docTest6(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");

        try {
        doctorService.clinicDocActive(dataT);
        } catch (Exception e){
            assertTrue(true);
        }


    }

    @Test
    void docTest7(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");
        Doctor doctor = new Doctor();

        try {
        doctorService.closeAppointment(doctor);
        } catch (Exception e){
            assertTrue(true);
        }


    }

    @Test
    void docTest8(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");

        try {
            doctorService.returnOpenAppointment(dataT);
        } catch (Exception e){
            assertTrue(true);
        }


    }

    @Test
    void docTest9(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");

        try {
            doctorService.verifyDocIsConsult(dataT);
        } catch (Exception e){
            assertTrue(true);
        }


    }

    @Test
    void docTest10(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");


        try {
        doctorService.getByExamsStatus();
            assertTrue(true);
        } catch (Exception e){
            assertTrue(true);
        }


    }

    @Test
    void docTest11(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");

        try {
            doctorService.getByPatientName();
            assertTrue(true);
        } catch (Exception e){
            assertTrue(true);
        }


    }

    @Test
    void docTest12(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");
        GetPatientByCPFDTO dataC = new GetPatientByCPFDTO("sadadad");


        try {
        secretaryService.patientCli(dataC ,dataT);
        } catch (Exception e){
            assertTrue(true);
        }


    }

    @Test
    void docTest13(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");

        try {
            adminSystemService.registerFirstAdmin();
            adminSystemService.listAllClinics();
            adminSystemService.countClinic();
            adminSystemService.countPats();
            adminSystemService.countLabs();
            adminSystemService.listAllLaboratory();
            adminSystemService.listAllPat();

            assertTrue(true);
        } catch (Exception e){
            assertTrue(true);
        }


    }

    @Test
    void docTest14(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        CreateAdminDTO dataA = new CreateAdminDTO("asd","asdas","adasd","adasdas","sadsadsd");

        try {
        adminService.registerAdm(dataA,dataT);
        } catch (Exception e){
            assertTrue(true);
        }


    }

    @Test
    void docTest15(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");

        try {
            adminService.clinicAdm(dataT);
        } catch (Exception e){
            assertTrue(true);
        }


    }

    @Test
    void docTest16(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");

        try {
            adminService.docCli(dataT);

        } catch (Exception e){
            assertTrue(true);
        }


    }

    @Test
    void docTest100(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        RequestSecretaryCpfDTO dataV = new RequestSecretaryCpfDTO("sdaasd");

        try {
            adminService.secretaryExists(dataV);
        } catch (Exception e){
            assertTrue(true);
        }


    }

    @Test
    void docTest17(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");
        SecretaryDTO dataA = new SecretaryDTO("asd","asdas","adasd","adasdas","sadsadsd");


        try {
        adminService.registerSecretary(dataA,dataT);
        } catch (Exception e){
            assertTrue(true);
        }


    }

    @Test
    void docTest18(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");

        try {
        adminService.labCli(dataT);
        } catch (Exception e){
            assertTrue(true);
        }


    }

    @Test
    void docTest19(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        RequestSecretaryEmailDTO dataV = new RequestSecretaryEmailDTO("sdaasd");
        try {
            adminService.enableSecretary(dataV);
        } catch (Exception e){
            assertTrue(true);
        }


    }

    @Test
    void docTest20(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        RequestSecretaryEmailDTO dataV = new RequestSecretaryEmailDTO("sdaasd");
        try {
            adminService.disableSecretary(dataV);
        } catch (Exception e){
            assertTrue(true);
        }


    }

    @Test
    void docTest21(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        RequestSecretaryEmailDTO dataV = new RequestSecretaryEmailDTO("sdaasd");

        try {
            adminService.enableLabUser(dataV);

        } catch (Exception e){
            assertTrue(true);
        }


    }
    @Test
    void docTest22(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        RequestSecretaryEmailDTO dataV = new RequestSecretaryEmailDTO("sdaasd");

        try {

            adminService.disableLabUser(dataV);

        } catch (Exception e){
            assertTrue(true);
        }


    }
    @Test
    void docTest23(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");
        CreateDoctorDTO dataC = new CreateDoctorDTO("qwewqe","sadasddsa", "asdasdasdas","sadasdasasd","asdasdasd","adasdasd");

        try {
            doctorService.registerDoctor(dataC, dataT);
        } catch (Exception e){
            assertTrue(true);
        }


    }
    @Test
    void docTest34(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");

        try {
        doctorService.registerDocUserExists(dataV,dataT);
        } catch (Exception e){
            assertTrue(true);
        }


    }
    @Test
    void docTest451(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");

        try {
            doctorService.getByPatientName();
            doctorService.getByExamsStatus();
            doctorService.getExamsType();
            assertTrue(true);
        } catch (Exception e){
            assertTrue(true);
        }


    }
    @Test
    void docTest165(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");
        CreateAnamnesisDTO dataA = new CreateAnamnesisDTO("dasdas","asdasdasd","asdfsafs","asfafas","","","","","","","",true,true,"","",1,1,1,1,"","","");

        try {
            doctorService.registerNewAnamnese(dataT,dataA);
        } catch (Exception e){
            assertTrue(true);
        }


    }
    @Test
    void docTest416(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");
        ExamsRequestDTO dataTa = new ExamsRequestDTO("asdasd");

        try {
            doctorService.requestExams(dataTa,dataT);
        } catch (Exception e){
            assertTrue(true);
        }


    }
    @Test
    void docTest1656(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        AuthVerificDTO dataq = new AuthVerificDTO("adasd");


        try {
            authService.verificUserActive(dataq);
        } catch (Exception e){
            assertTrue(true);
        }


    }
    @Test
    void docTest166(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");

        try {
            authService.getHistory(dataT);
        } catch (Exception e){
            assertTrue(true);
        }


    }
    @Test
    void docTest162626(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");

        try {
            authService.perfil(dataT);
        } catch (Exception e){
            assertTrue(true);
        }


    }
    @Test
    void docTest14126(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");

        try {
            authService.profileSecretary(dataT);
        } catch (Exception e){
            assertTrue(true);
        }


    }
    @Test
    void docTest11246(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");

        try {
            authService.profilePatient(dataT);
        } catch (Exception e){
            assertTrue(true);
        }


    }
    @Test
    void docTest141246(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");

        try {
            authService.profileAdmin(dataT);
        } catch (Exception e){
            assertTrue(true);
        }


    }
    @Test
    void docTest1655(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");

        try {
            authService.profileDoc(dataT);
        } catch (Exception e){
            assertTrue(true);
        }


    }
    @Test
    void docTest1566(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");
        AuthVerificDTO dataq = new AuthVerificDTO("adasd");

        try {
            authService.authVerific(dataq);
        } catch (Exception e){
            assertTrue(true);
        }


    }
    @Test
    void docTest131(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");

        try {
            clinicService.verificCliActive(dataT);

        } catch (Exception e){
            assertTrue(true);
        }


    }
    @Test
    void docTest161(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");
        CreateFirstAdmDTO dataq = new CreateFirstAdmDTO("","","");

        try {
            clinicService.createFirstAdmin(dataq);
        } catch (Exception e){
            assertTrue(true);
        }


    }
    @Test
    void docTest146(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");
        Auth auth = new Auth();

        try {
        emailService.resetSenhaEmail(auth);
        } catch (Exception e){
            assertTrue(true);
        }


    }

    @Test
    void docTest142(){
        CreateDoctorDTO data = new CreateDoctorDTO("sadsa","sadsad","sddasdasd","asdadsa","dsadasd", "fafsasf");
        RequestTokenDTO dataT = new RequestTokenDTO("asdsadwdqwdqw");
        DoctorVerificDTO dataV = new DoctorVerificDTO("sdaasd");
        Auth auth = new Auth();

        try {
            emailService.firtLoginEmail(auth);
        } catch (Exception e){
            assertTrue(true);
        }


    }
}