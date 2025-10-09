package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.domain.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminRepository extends JpaRepository <Admin, UUID> {
}
