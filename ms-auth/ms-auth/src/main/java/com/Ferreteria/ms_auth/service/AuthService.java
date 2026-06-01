package com.Ferreteria.ms_auth.service;

import com.Ferreteria.ms_auth.dto.AuthResponse;
import com.Ferreteria.ms_auth.dto.LoginRequest;
import com.Ferreteria.ms_auth.dto.RegisterRequest;
import com.Ferreteria.ms_auth.model.Usuario;
import com.Ferreteria.ms_auth.repository.UsuarioRepository;
import com.Ferreteria.ms_auth.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("Credenciales inválidas"));

        if (!passwordEncoder.matches(
                request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(
                usuario.getUsername(), usuario.getRol());

        return AuthResponse.builder()
                .token(token)
                .username(usuario.getUsername())
                .rol(usuario.getRol())
                .build();
    }

    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El usuario ya existe");
        }

        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(request.getRol())
                .build();

        usuarioRepository.save(usuario);

        String token = jwtUtil.generateToken(
                usuario.getUsername(), usuario.getRol());

        return AuthResponse.builder()
                .token(token)
                .username(usuario.getUsername())
                .rol(usuario.getRol())
                .build();
    }

    public List<AuthResponse> getAll() {
        return usuarioRepository.findAll().stream()
                .map(u -> AuthResponse.builder()
                        .username(u.getUsername())
                        .rol(u.getRol())
                        .build())
                .collect(Collectors.toList());
    }
}

