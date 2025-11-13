package com.SCX.ControleDeExame.infra.security;

import com.SCX.ControleDeExame.repository.AuthRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenService tokenService;

    @Autowired
    AuthRepository authRepository;

    // 🔹 Lista de endpoints públicos (não exigem token)
    private static final List<String> PUBLIC_PATHS = List.of(
            "/auth/login",
            "/auth/first-login",
            "/auth/verificUserExists",
            "/auth/verificUserActive",
            "/consult/getCep",
            "/consult/getCnpj",
            "/resetPassword",
            "/clinic/getCliActive",
            "/files/download",
            "/files/preview"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // 🔸 Ignora endpoints públicos
        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = recoverToken(request);
        if (token != null) {
            try {
                var id = tokenService.registerUser(token);
                UserDetails auth = authRepository.findById(UUID.fromString(id))
                        .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

                var authentication = new UsernamePasswordAuthenticationToken(auth, null, auth.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // Evita quebrar fluxo por token inválido
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        // verifica se o caminho começa com algum prefixo público
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        return authHeader.replace("Bearer ", "");
    }
}
