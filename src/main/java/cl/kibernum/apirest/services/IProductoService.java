package cl.kibernum.apirest.services;

import java.util.List;

import cl.kibernum.apirest.entities.Producto;

public interface IProductoService {
     List<Producto> findAllByActiveTrue();  
}
