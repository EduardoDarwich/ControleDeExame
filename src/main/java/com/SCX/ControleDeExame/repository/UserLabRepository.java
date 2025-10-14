package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.domain.doctor.Doctor;
import com.SCX.ControleDeExame.domain.user_lab.UserLab;
import com.SCX.ControleDeExame.domain.user_lab.UserLabId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserLabRepository extends JpaRepository<UserLab, UserLabId> {
    UserLab findByAuthId_Id(UUID id);
}
