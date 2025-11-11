package com.SCX.ControleDeExame.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {

        return httpSecurity.csrf(csrf -> csrf.disable())
                .cors(cors -> {})
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize


                        .requestMatchers(HttpMethod.POST, "admin/registerAdmin").hasRole("Admin")
                        .requestMatchers(HttpMethod.POST, "admin/registerSecretary").hasRole("Admin")
                        .requestMatchers(HttpMethod.POST, "admin/verificSecretaryExists").hasRole("Admin")
                        .requestMatchers(HttpMethod.POST, "admin/doctorClinic").hasRole("Admin")
                        .requestMatchers(HttpMethod.POST, "admin/verificLabCli").hasRole("Admin")
                        .requestMatchers(HttpMethod.POST, "admin/verificLabExists").hasRole("Admin")
                        .requestMatchers(HttpMethod.POST, "admin/transferLab").hasRole("Admin")
                        .requestMatchers(HttpMethod.POST, "admin/getLabCli").hasRole("Admin")
                        .requestMatchers(HttpMethod.POST, "admin/getSecretary").hasRole("Admin")
                        .requestMatchers(HttpMethod.POST, "admin/disableSecretary").hasRole("Admin")
                        .requestMatchers(HttpMethod.POST, "admin/enableSecretary").hasRole("Admin")
                        .requestMatchers(HttpMethod.POST, "admin/disableLaboratory").hasRole("Admin")
                        .requestMatchers(HttpMethod.POST, "admin/enableLaboratory").hasRole("Admin")
                        .requestMatchers(HttpMethod.POST, "admin/disableUserLab").hasRole("Admin")
                        .requestMatchers(HttpMethod.POST, "admin/enableUserLab").hasRole("Admin")
                        .requestMatchers(HttpMethod.POST, "admin/clinicAdm").hasRole("Admin")


                        .requestMatchers(HttpMethod.PATCH, "/adminSystem/disableClinic").hasRole("AdminSystem")
                        .requestMatchers(HttpMethod.PATCH, "/adminSystem/enableClinic").hasRole("AdminSystem")
                        .requestMatchers(HttpMethod.GET, "/adminSystem/getAllCli").hasRole("AdminSystem")
                        .requestMatchers(HttpMethod.GET, "/adminSystem/getAllLab").hasRole("AdminSystem")
                        .requestMatchers(HttpMethod.GET, "/adminSystem/getAllPat").hasRole("AdminSystem")
                        .requestMatchers(HttpMethod.GET, "/adminSystem/getCountLab").hasRole("AdminSystem")
                        .requestMatchers(HttpMethod.GET, "/adminSystem/getCountCli").hasRole("AdminSystem")
                        .requestMatchers(HttpMethod.GET, "/adminSystem/getCountPat").hasRole("AdminSystem")
                        .requestMatchers(HttpMethod.PATCH, "/adminSystem/enableAdmCli").hasRole("AdminSystem")
                        .requestMatchers(HttpMethod.PATCH, "/adminSystem/disableAdmCli").hasRole("AdminSystem")
                        .requestMatchers(HttpMethod.PATCH, "/adminSystem/registerUser").hasRole("AdminSystem")
                        .requestMatchers(HttpMethod.PATCH, "/adminSystem/disableLabSyst").hasRole("AdminSystem")
                        .requestMatchers(HttpMethod.PATCH, "/adminSystem/enableLabSyst").hasRole("AdminSystem")


                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/first-login/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/perfil").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/verificUserExists").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/verificUserActive").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/getHistory").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/getProfileDoctor").hasRole("Doctor")
                        .requestMatchers(HttpMethod.GET, "/auth/getProfilePatient").hasRole("Patient")
                        .requestMatchers(HttpMethod.GET, "/auth/getProfileSecretary").hasRole("Secretary")
                        .requestMatchers(HttpMethod.GET, "/auth/getProfileAdmin").hasRole("Admin")
                        .requestMatchers(HttpMethod.PATCH, "/auth/updatePat").hasRole("Patient")
                        .requestMatchers(HttpMethod.PATCH, "/auth/updateDoc").hasRole("Doctor")
                        .requestMatchers(HttpMethod.PATCH, "/auth/updateSecretary").hasRole("Secretary")
                        .requestMatchers(HttpMethod.PATCH, "/auth/updateAdmin").hasRole("Admin")


                        .requestMatchers(HttpMethod.POST, "/clinic/create").hasRole("AdminSystem")
                        .requestMatchers(HttpMethod.POST, "/clinic/firstAdm").hasRole("AdminSystem")
                        .requestMatchers(HttpMethod.GET, "/clinic/getCliActive").permitAll()


                        .requestMatchers(HttpMethod.POST, "/consult/getCep").permitAll()
                        .requestMatchers(HttpMethod.POST, "/consult/getCnpj").permitAll()

                        .requestMatchers(HttpMethod.POST, "/doctor/register").hasRole("Admin")
                        .requestMatchers(HttpMethod.POST, "/doctor/requestExm").hasRole("Doctor")
                        .requestMatchers(HttpMethod.POST, "/doctor/searchDoc").hasRole("Admin")
                        .requestMatchers(HttpMethod.GET, "/doctor/getRequestExamPendent").hasRole("Doctor")
                        .requestMatchers(HttpMethod.POST, "/doctor/getByCrm").hasRole("Admin")
                        .requestMatchers(HttpMethod.GET, "/doctor/getAppointmentOpen").hasRole("Doctor")
                        .requestMatchers(HttpMethod.POST, "/doctor/transferDoctor").hasRole("Admin")
                        .requestMatchers(HttpMethod.GET, "/doctor/clinicsDoctor").hasRole("Doctor")
                        .requestMatchers(HttpMethod.PATCH, "/doctor/updateClinicDocPresent").hasRole("Doctor")
                        .requestMatchers(HttpMethod.GET, "/doctor/getClinicActive").hasRole("Doctor")
                        .requestMatchers(HttpMethod.GET, "/doctor/getLabDocCli").hasRole("Doctor")
                        .requestMatchers(HttpMethod.GET, "/doctor/getExamsType").hasRole("Doctor")
                        .requestMatchers(HttpMethod.POST, "/doctor/openConsultation").hasRole("Doctor")
                        .requestMatchers(HttpMethod.PATCH, "/doctor/closeConsultation").hasRole("Doctor")
                        .requestMatchers(HttpMethod.POST, "/doctor/registerAnamnese").hasRole("Doctor")
                        .requestMatchers(HttpMethod.POST, "/doctor/bmiCalculator").hasRole("Doctor")
                        .requestMatchers(HttpMethod.POST, "/doctor/createCustomField").hasRole("Doctor")
                        .requestMatchers(HttpMethod.GET, "/doctor/getAppointmentsPat").hasRole("Doctor")
                        .requestMatchers(HttpMethod.GET, "/doctor/verifyDocIsConsult").hasRole("Doctor")
                        .requestMatchers(HttpMethod.POST, "/doctor/createExams").hasRole("Doctor")


                        .requestMatchers(HttpMethod.POST, "/files/upload").hasRole("LaboratoryUser")
                        .requestMatchers(HttpMethod.GET, "/files/download/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/files/preview/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/files/examsRequestPDF").permitAll()


                        .requestMatchers(HttpMethod.POST, "/laboratory/register").hasRole("Admin")
                        .requestMatchers(HttpMethod.POST, "/laboratory/register/Adm").hasRole("Admin")
                        .requestMatchers(HttpMethod.POST, "/laboratory/register/User").hasRole("LaboratoryAdmin")
                        .requestMatchers(HttpMethod.GET, "/laboratory/clinicsLab").hasRole("LaboratoryAdmin")
                        .requestMatchers(HttpMethod.POST, "/laboratory/uploadExam").hasRole("LaboratoryUser")
                        .requestMatchers(HttpMethod.GET, "/laboratory/getLabActive").permitAll()

                        .requestMatchers(HttpMethod.GET, "/notification/getNoRead").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/notification/markRead").permitAll()

                        .requestMatchers(HttpMethod.GET, "/patient/getRequestExamPendent").hasRole("Patient")
                        .requestMatchers(HttpMethod.GET, "/patient/getExamsResult").hasRole("Patient")
                        .requestMatchers(HttpMethod.PATCH, "/patient/anonimizePat").hasRole("Patient")

                        .requestMatchers(HttpMethod.POST, "/prontuario/getAnamneseConsult").hasRole("Doctor")
                        .requestMatchers(HttpMethod.POST, "/prontuario/getDiagnostic").hasRole("Doctor")
                        .requestMatchers(HttpMethod.POST, "/prontuario/getExamsRequest").hasRole("Doctor")
                        .requestMatchers(HttpMethod.POST, "/prontuario/getExams").hasRole("Doctor")

                        .requestMatchers(HttpMethod.PATCH, "/resetPassword/generateToken").permitAll()
                        .requestMatchers(HttpMethod.POST, "/resetPassword/resetPassword/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/secretary/registerPatient").hasRole("Secretary")
                        .requestMatchers(HttpMethod.GET, "/secretary/verificPatCli").hasRole("Secretary")
                        .requestMatchers(HttpMethod.GET, "/secretary/verificPatSyst").hasRole("Secretary")
                        .requestMatchers(HttpMethod.GET, "/secretary/transferPat").hasRole("Secretary")
                        .requestMatchers(HttpMethod.GET, "/secretary/clinicSecretary").hasRole("Secretary")
                        .requestMatchers(HttpMethod.GET, "/secretary/getPatientsCli").hasRole("Secretary")
                        .requestMatchers(HttpMethod.GET, "/secretary/getDocsAvailable").hasRole("Secretary")
                        .requestMatchers(HttpMethod.GET, "/secretary/openAppointment").hasRole("Secretary")

                        .anyRequest().permitAll()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    @Bean
    public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }
}
