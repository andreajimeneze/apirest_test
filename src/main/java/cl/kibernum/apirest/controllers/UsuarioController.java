package cl.kibernum.apirest.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.kibernum.apirest.entities.Usuario;
import cl.kibernum.apirest.exception.ResourceNotFoundException;
import cl.kibernum.apirest.services.UsuarioServiceImpl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {
    private UsuarioServiceImpl usuarioService;

    public UsuarioController(UsuarioServiceImpl usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> getAllUsers() {
        return ResponseEntity.ok(usuarioService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUserById(@RequestParam int id) {
        Usuario usuario = usuarioService.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return ResponseEntity.ok(usuario);
    }

    @PatchMapping("/{email}")
    public ResponseEntity<Void> softDeleteUsuario(@PathVariable String email) {
        Usuario usuario = usuarioService.findByEmailAndActiveTrue(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        usuarioService.softDelete(usuario.getId());
        return ResponseEntity.noContent().build();
    }

}
