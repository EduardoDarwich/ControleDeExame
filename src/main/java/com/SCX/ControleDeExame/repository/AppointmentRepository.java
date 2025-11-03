package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.dataTransferObject.appointmentDTO.GetAppointmentOpenDocDTO;
import com.SCX.ControleDeExame.dataTransferObject.appointmentDTO.ReturnAppointmentsPatDTO;
import com.SCX.ControleDeExame.domain.appointment.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    @Query("""
            select a
            from Appointment a
            join a.doctor d
            where d.id = :doctorId and a.openAppointment = true
            """)
    Appointment findByDoctorAvaiable(@Param("doctorId") UUID doctorId);

    @Query("""
            select new com.SCX.ControleDeExame.dataTransferObject.appointmentDTO.GetAppointmentOpenDocDTO(a.name, o.dateCreate)
            from Appointment o
            join o.doctor d
            join o.patient p
            join p.authId a
            where d.id = :doctorId and o.openAppointment = true
            """)
    GetAppointmentOpenDocDTO findByDoctorAppointmentOpen(@Param("doctorId") UUID doctorId);

    @Query("""
            select new com.SCX.ControleDeExame.dataTransferObject.appointmentDTO.ReturnAppointmentsPatDTO(
            ad.name,
            d.specialty,
            a.dateEnd,
            c.name,
            a.id
            )
            from Appointment a
            join a.clinic c
            join a.doctor d
            join d.authId ad
            join a.patient p
            where p.id = :patientId and a.openAppointment = false
            """)
    List<ReturnAppointmentsPatDTO> findAppointmentByPatient(@Param("patientId") UUID patientId);




}
