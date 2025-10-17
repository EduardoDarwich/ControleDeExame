package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.ResponseClinicMedDTO;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository <Doctor, UUID> {
    Doctor findByAuthId_Id(UUID id);
    Doctor findByCrm(String crm);
    boolean existsByCrm(String crm);

    @Query("""
            select new com.SCX.ControleDeExame.dataTransferObject.doctorDTO.ResponseClinicMedDTO(c.name)
            from Clinic c
            join c.doctors d
            where d.id = :doctorId
            """)
    List<ResponseClinicMedDTO> findClinicByDoctor(@Param("doctorId") UUID doctorId);

}
