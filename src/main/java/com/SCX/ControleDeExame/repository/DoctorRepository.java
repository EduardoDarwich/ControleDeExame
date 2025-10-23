package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.ResponseClinicDocDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.ResponseDocCliLabDTO;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository <Doctor, UUID> {
    Doctor findByAuthId_Id(UUID id);
    Doctor findByCrm(String crm);
    boolean existsByCrm(String crm);

    @Query("""
            select new com.SCX.ControleDeExame.dataTransferObject.doctorDTO.ResponseClinicDocDTO(c.name,c.cnpj)
            from Clinic c
            join c.doctors d
            where d.id = :doctorId
            """)
    List<ResponseClinicDocDTO> findClinicByDoctor(@Param("doctorId") UUID doctorId);

    @Query("""
            select new com.SCX.ControleDeExame.dataTransferObject.doctorDTO.ResponseDocCliLabDTO(l.name)
            from Laboratory l
            join l.clinics c
            where c.id = :clinicId
            """)
    List<ResponseDocCliLabDTO>findLabByClinicDoc(@Param("clinicId") UUID clinicId);
}
