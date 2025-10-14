package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.domain.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Role findByName(String name);
}
