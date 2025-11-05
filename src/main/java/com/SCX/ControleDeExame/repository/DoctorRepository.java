package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.DoctorRequestExamDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.DoctorResultExamDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.ResponseClinicDocDTO;
import com.SCX.ControleDeExame.dataTransferObject.doctorDTO.ResponseDocCliLabDTO;
import com.SCX.ControleDeExame.dataTransferObject.laboratoryDTO.LaboratoryRequestExamDTO;
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
            select count(c) > 0
            from Consultation c
            join c.appointment a
            join a.doctor d
            where d.id = :doctorId and c.finished = false
            """)
    boolean findDocIsConsult(@Param("doctorId") UUID doctorId);

    @Query("""
            select new com.SCX.ControleDeExame.dataTransferObject.doctorDTO.ResponseDocCliLabDTO(l.name)
            from Laboratory l
            join l.clinics c
            where c.id = :clinicId
            """)
    List<ResponseDocCliLabDTO>findLabByClinicDoc(@Param("clinicId") UUID clinicId);

    @Query("""
            select new com.SCX.ControleDeExame.dataTransferObject.doctorDTO.DoctorRequestExamDTO(
            pa.name,
            c.name,
            l.name,
            e.status,
            e.complement,
            e.examType,
            e.sampleType,
            e.requestDate
            )
            from Doctor d
            join d.examsRequests e
            join e.patientId p
            join p.authId pa
            join e.clinicId c
            join e.laboratoryId l
            where d.id = :doctorId and e.status = 'Pendente' and e.clinicId.id = d.idClinic
            """)
    List<DoctorRequestExamDTO> findRequestExamByDoctor(@Param("doctorId") UUID doctorId);

    @Query("""
            select new com.SCX.ControleDeExame.dataTransferObject.doctorDTO.DoctorResultExamDTO(
            ex.cid,
            ex.result_value,
            ex.observation
            )
            from Doctor d
            join d.examsRequests e
            join e.exams ex
            where d.id = :doctorId and e.clinicId.id = d.idClinic
            """)
    List<DoctorResultExamDTO> findByResultExamDoctor(@Param("doctorId") UUID doctorId);

}
