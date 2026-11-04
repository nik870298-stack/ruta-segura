package com.example.rutasegura.controller;

import com.example.rutasegura.dto.AuthReq;
import com.example.rutasegura.dto.AuthRes;
import com.example.rutasegura.dto.RegisterReq;
import com.example.rutasegura.model.Rol;
import com.example.rutasegura.model.Usuario;
import com.example.rutasegura.repository.UsuarioRepository;
import com.example.rutasegura.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para registro y login de usuarias")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/registro")
    @Operation(summary = "Registrar nueva usuaria (Rol USER predeterminado)")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegisterReq req) {
        if (usuarioRepository.existsByEmail(req.email())) {
            return ResponseEntity.badRequest().body("El email ya está registrado");
        }
        Usuario u = new Usuario(req.email(), passwordEncoder.encode(req.password()), req.nombre(), Rol.ROLE_USER);
        usuarioRepository.save(u);
        String token = jwtService.generateToken(u.getEmail(), u.getRol());
        return ResponseEntity.ok(new AuthRes(token, u.getEmail(), u.getNombre(), u.getRol()));
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión y obtener JWT")
    public ResponseEntity<?> login(@Valid @RequestBody AuthReq req) {
        var usuarioOpt = usuarioRepository.findByEmail(req.email());
        if (usuarioOpt.isEmpty() || !passwordEncoder.matches(req.password(), usuarioOpt.get().getPassword())) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
        Usuario u = usuarioOpt.get();
        String token = jwtService.generateToken(u.getEmail(), u.getRol());
        return ResponseEntity.ok(new AuthRes(token, u.getEmail(), u.getNombre(), u.getRol()));
    }
}