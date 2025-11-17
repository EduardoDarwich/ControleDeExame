package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.dataTransferObject.profileDTO.ProfileAdminDTO;
import com.SCX.ControleDeExame.dataTransferObject.profileDTO.ProfileDoctorDTO;
import com.SCX.ControleDeExame.dataTransferObject.profileDTO.ProfilePatientDTO;
import com.SCX.ControleDeExame.dataTransferObject.profileDTO.ProfileSecretaryDTO;
import com.SCX.ControleDeExame.domain.auth.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository <Auth, UUID> {
    UserDetails findByUsernameKey (String usernameKey);

    Auth findByToken(String token);
    Optional<Auth> findAuthByUsernameKey(String usernameKey);
    boolean existsByUsernameKey(String usernameKey);

    @Query("""
            select new com.SCX.ControleDeExame.dataTransferObject.profileDTO.ProfileDoctorDTO(
            d.crm,
            d.telephone,
            a.name,
            a.usernameKey
            )
            from Doctor d
            join d.authId a
            where d.id = :doctorId
            """)
    ProfileDoctorDTO findProfileDoctor (@Param("doctorId") UUID doctorId);

    @Query("""
            select new com.SCX.ControleDeExame.dataTransferObject.profileDTO.ProfilePatientDTO(
            p.dateBirth,
            p.telephone,
            p.cpf,
            a.usernameKey,
            a.name
            
            )
            from Patient p
            join p.authId a
            where p.id = :patientId
            """)
    ProfilePatientDTO findProfilePatient (@Param("patientId") UUID patientId);

    @Query("""
            select new com.SCX.ControleDeExame.dataTransferObject.profileDTO.ProfileAdminDTO(
            a.cpf,
            a.telephone,
            c.name,
            au.name,
            au.usernameKey
            
            )
            from Admin a
            join a.clinicId c
            join a.authId au
            where a.id = :adminId
            """)
    ProfileAdminDTO findProfileAdmin (@Param("adminId") UUID adminId);

    @Query("""
            select new com.SCX.ControleDeExame.dataTransferObject.profileDTO.ProfileSecretaryDTO(
            s.cpf,
            s.telephone,
            c.name,
            a.usernameKey,
            a.name
            )
            from Secretary s
            join s.clinicId c
            join s.authId a
            where s.id = :secretaryId
            """)
    ProfileSecretaryDTO findProfileSecretary (@Param("secretaryId") UUID secretaryId);



}




