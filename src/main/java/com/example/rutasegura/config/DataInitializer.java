package com.example.rutasegura.config;

import com.example.rutasegura.model.Rol;
import com.example.rutasegura.model.Usuario;
import com.example.rutasegura.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!usuarioRepository.existsByEmail("admin@rutasegura.gov.co")) {
                Usuario admin = new Usuario(
                        "admin@rutasegura.gov.co",
                        passwordEncoder.encode("AdminPass123!"),
                        "Administradora Sistema",
                        Rol.ROLE_ADMIN
                );
                usuarioRepository.save(admin);
            }
        };
    }
}