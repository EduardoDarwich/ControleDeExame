package com.SCX.ControleDeExame.infra.security;

import com.SCX.ControleDeExame.domain.auth.Auth;
import com.SCX.ControleDeExame.domain.role.Role;
import com.SCX.ControleDeExame.repository.AuthRepository;
import com.SCX.ControleDeExame.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserAdminSystemCreate implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthRepository authRepository;

    @Override
    public void run(String... args) throws Exception {
        Role adminSystem = roleRepository.findByName("AdminSystem");
        Auth newAuth = new Auth();
        String encryptedPassword = new BCryptPasswordEncoder().encode("123456789");

        newAuth.setPassword_key(encryptedPassword);
        newAuth.setUsernameKey("adminsistema@gmail.com");
        newAuth.setName("Eduardo");
        newAuth.setActive(true);
        newAuth.setLocked(false);
        newAuth.getRoles().add(adminSystem);
        authRepository.save(newAuth);
    }
}
