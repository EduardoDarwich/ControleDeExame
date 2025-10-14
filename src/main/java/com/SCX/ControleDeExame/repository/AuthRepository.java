package com.SCX.ControleDeExame.repository;

import com.SCX.ControleDeExame.domain.auth.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface AuthRepository extends JpaRepository <Auth, UUID> {
    UserDetails findByUsernameKey (String usernameKey);

    Auth findByToken(String token);
    Optional<Auth> findAuthByUsernameKey(String usernameKey);



}
