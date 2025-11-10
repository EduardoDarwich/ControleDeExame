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

                        .requestMatchers(HttpMethod.POST, "/patient/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/laboratory/register/User").hasRole("LaboratoryAdimin")


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


                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/first-login").permitAll()
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


                        .requestMatchers(HttpMethod.PATCH, "/clinic/updateAdmin").hasRole("Admin")








                        .requestMatchers(HttpMethod.GET, "/patient/GetAllPatient").permitAll()
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
