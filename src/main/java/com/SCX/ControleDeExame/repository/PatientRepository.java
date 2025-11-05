package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.DoctorRequestExamDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.DoctorResultExamDTO;
import com.SCX.ControleDeExame.dataTransferObject.patientDTO.PatientRequestExamDTO;
import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PatientRepository extends JpaRepository <Patient, UUID> {
    Patient findByCpf(String cpf);
    Patient findByAuthId_Id(UUID id);
    boolean existsByCpf(String cpf);

    @Query("""
            select new com.SCX.ControleDeExame.dataTransferObject.patientDTO.PatientRequestExamDTO(
            da.name,
            c.name,
            l.name,
            e.status,
            e.complement,
            e.examType,
            e.sampleType,
            e.requestDate
            )
            from Patient p
            join p.examsRequests e
            join e.doctorId d
            join d.authId da
            join e.clinicId c
            join e.laboratoryId l
            where p.id = :patientId and e.status = 'Pendente'
            """)
    List<PatientRequestExamDTO> findRequestExamByPatient(@Param("patientId") UUID patientId);

  /*  @Query("""
            select new com.SCX.ControleDeExame.dataTransferObject.doctorDTO.DoctorResultExamDTO(
            ex.cid,
            ex.result_value,
            ex.observation
            )
            from Patient p
            join p.examsRequests e
            join e.exams ex
            where p.id = :patientId
            """)
    List<DoctorResultExamDTO> findResultExamByPatient (@Param("patientId") UUID patientId);*/




}


