package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.domain.clinic.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ClinicRepository extends JpaRepository<Clinic, UUID> {
    Clinic findByCnpj(String cnpj);

    @Query(""" 
            select case when count(c) > 0 then true else false end
            from Clinic c join c.doctors d
            where c.id = :clinicId and d.id = :doctorId
            """
    )
    boolean existsDoctorClinic(@Param("clinicId") UUID clinicId,
                               @Param("doctorId") UUID doctorId);
}
