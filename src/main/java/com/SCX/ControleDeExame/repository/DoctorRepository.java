package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.domain.doctor.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DoctorRepository extends JpaRepository <Doctor, UUID> {
    Doctor findByAuthId_Id(UUID id);

}
