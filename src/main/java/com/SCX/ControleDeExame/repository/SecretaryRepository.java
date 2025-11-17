package com.SCX.ControleDeExame.repository;


import com.SCX.ControleDeExame.domain.admin.Admin;
import com.SCX.ControleDeExame.domain.secretary.Secretary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SecretaryRepository extends JpaRepository <Secretary, UUID> {
    Secretary findByAuthId_Id(UUID id);

    boolean existsByCpf(String cpf);

    boolean existsByTelephone(String telephone);

}
