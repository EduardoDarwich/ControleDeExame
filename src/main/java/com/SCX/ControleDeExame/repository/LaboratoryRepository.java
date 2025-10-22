package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.ResponseClinicDocDTO;
import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.ResponseClinicLabDTO;
import com.SCX.ControleDeExame.domain.laboratory.Laboratory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LaboratoryRepository extends JpaRepository <Laboratory, UUID> {
    Laboratory findByCnpj(String cnpj);
    boolean existsByCnpj(String cnpj);

    @Query("""
            select new com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.ResponseClinicLabDTO(c.name, c.cnpj)
            from Clinic c
            join c.laboratories l
            where l.id = :laboratoryId
            """)
    List<ResponseClinicLabDTO> findClinicByLaboratory(@Param("laboratoryId") UUID laboratoryId);
}
