package cl.kibernum.apirest.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import cl.kibernum.apirest.dto.UsuarioDto;
import cl.kibernum.apirest.entities.Usuario;
import cl.kibernum.apirest.exception.ResourceNotFoundException;
import cl.kibernum.apirest.repositories.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements ICrudService<Usuario, UsuarioDto>, IUsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public List<Usuario> getAll() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> getById(int id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario create(UsuarioDto dDto) {
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public Usuario update(int id, UsuarioDto dDto) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void softDelete(int id) {
       Usuario searchingUsuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        searchingUsuario.desactivateUser();
        usuarioRepository.save(searchingUsuario);
    }

    @Override
    public Optional<Usuario> findByEmailAndActiveTrue(String email) {
        return usuarioRepository.findByEmailAndActiveTrue(email);
    }
    
}
