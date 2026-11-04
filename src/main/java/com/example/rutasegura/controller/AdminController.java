package com.example.rutasegura.controller;

import com.example.rutasegura.dto.CambiarRolReq;
import com.example.rutasegura.model.Usuario;
import com.example.rutasegura.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Administración", description = "Gestión exclusiva para Administradores (ROLE_ADMIN)")
public class AdminController {

    private final UsuarioRepository usuarioRepository;

    public AdminController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/usuarios")
    @Operation(summary = "Listar todas las cuentas registradas")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @PatchMapping("/usuarios/{id}/rol")
    @Operation(summary = "Asignar rol de Moderador o Administrador a un usuario")
    public ResponseEntity<?> cambiarRol(@PathVariable Long id, @Valid @RequestBody CambiarRolReq req) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setRol(req.nuevoRol());
            usuarioRepository.save(usuario);
            return ResponseEntity.ok("Rol actualizado correctamente a " + req.nuevoRol());
        }).orElse(ResponseEntity.notFound().build());
    }
}