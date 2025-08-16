package cl.kibernum.apirest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.kibernum.apirest.models.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    
} 