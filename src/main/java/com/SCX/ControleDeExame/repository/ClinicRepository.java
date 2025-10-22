package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseDocCliConsultDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseDocCliDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseLabCliDTO;
import com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponsePatCliDTO;
import com.SCX.ControleDeExame.domain.clinic.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ClinicRepository extends JpaRepository<Clinic, UUID> {
    Clinic findByCnpj(String cnpj);

    Clinic findByName(String name);

    @Query(""" 
            select case when count(c) > 0 then true else false end
            from Clinic c join c.doctors d
            where c.id = :clinicId and d.id = :doctorId
            """
    )
    boolean existsDoctorClinic(@Param("clinicId") UUID clinicId,
                               @Param("doctorId") UUID doctorId);

    @Query("""
            select case when count(c) > 0 then true else false end
            from Clinic c join c.patients p
            where c.id = :clinicId and p.id = :patientId
            """)
    boolean existsPatientClinic(@Param("clinicId") UUID clinicId,
                                @Param("patientId") UUID patientId);

    @Query("""
            select case when count(c) > 0 then true else false end
            from Clinic c join c.laboratories l
            where c.id = :clinicId and l.id = :laboratoryId
            """)
    boolean existsLaboratoryClinic(@Param("clinicId") UUID clinicId,
                                   @Param("laboratoryId") UUID laboratoryId);

    @Query("""
            select new com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseDocCliDTO(d.crm, a.name)
            from Doctor d
            join d.clinics c
            join d.authId a
            where c.id = :clinicId
            """)
    List<ResponseDocCliDTO> findDocByClinic(@Param("clinicId") UUID clinicId);

    @Query("""
            select new com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponsePatCliDTO(a.name, a.active)
            from Patient p
            join p.clinics c
            join p.authId a
            where c.id = :clinicId
            """)
    List<ResponsePatCliDTO> findPatByClinic(@Param("clinicId") UUID clinicId);

    @Query("""
            select new com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseLabCliDTO(l.cnpj, l.name)
            from Laboratory l
            join l.clinics c
            where c.id = :clinicId
            """)
    List<ResponseLabCliDTO> findLabByClinic(@Param("clinicId") UUID clinicId);

    @Query("""
            select new com.SCX.ControleDeExame.dataTransferObject.clinicDTO.ResponseDocCliConsultDTO(a.name, d.available, a.usernameKey)
            from Doctor d
            join d.clinics c
            join d.authId a
            where c.id = :clinicId and d.idClinic = :clinicId and d.available = true
            """)
    List<ResponseDocCliConsultDTO> findDocConsultByClinic(@Param("clinicId") UUID clinicId);


}