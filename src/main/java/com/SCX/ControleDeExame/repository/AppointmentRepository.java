package com.SCX.ControleDeExame.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<com.SCX.ControleDeExame.domain.appointment.Appointment, UUID> {
}
