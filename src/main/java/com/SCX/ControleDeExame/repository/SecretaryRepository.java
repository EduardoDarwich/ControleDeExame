package com.SCX.ControleDeExame.repository;


import com.SCX.ControleDeExame.domain.secretary.Secretary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SecretaryRepository extends JpaRepository <Secretary, UUID> {


}
