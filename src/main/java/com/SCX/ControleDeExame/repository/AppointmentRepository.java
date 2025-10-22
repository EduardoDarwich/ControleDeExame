package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.domain.appointment.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    @Query("""
            select a
            from Appointment a
            join a.doctor d
            where d.id = :doctorId and a.openAppointment = true
            """)
    Appointment findByDoctorAvaiable(@Param("doctorId") UUID doctorId);
}
