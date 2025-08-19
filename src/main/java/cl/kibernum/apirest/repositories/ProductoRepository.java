package cl.kibernum.apirest.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.kibernum.apirest.entities.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findAllByActiveTrue();  
    Optional<Producto> findByNombre(String nombre);
} 