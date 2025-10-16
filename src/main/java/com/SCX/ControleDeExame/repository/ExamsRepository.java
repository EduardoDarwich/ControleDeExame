package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.dataTransferObject.examsDTO.GetByDoctorDTO;
import com.SCX.ControleDeExame.domain.exams.Exams;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ExamsRepository extends JpaRepository<Exams, UUID> {
    Exams findByDoctorId (UUID id);

    @Query("""
           SELECT new com.SCX.ControleDeExame.dataTransferObject.examsDTO.GetByDoctorDTO(
           e.cid,
           e.status,
           e.result_value,
           e.result_file_url,
           e.observation
           )
           
           from Exams e
           JOIN e.patientId p
           JOIN e.laboratoryId l
           where e.doctor.id = :doctorId
            """)
    List<GetByDoctorDTO> findAllByDoctorId(@Param("doctorId") UUID doctorId);
}
