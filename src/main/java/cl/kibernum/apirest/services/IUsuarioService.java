package cl.kibernum.apirest.services;

import java.util.Optional;

import cl.kibernum.apirest.entities.Usuario;

public interface IUsuarioService {
    Optional<Usuario> findByEmailAndActiveTrue(String email);
}
