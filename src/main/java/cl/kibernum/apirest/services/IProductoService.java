package cl.kibernum.apirest.services;

import java.util.List;
import java.util.Optional;

import cl.kibernum.apirest.dto.ProductoDto;
import cl.kibernum.apirest.models.Producto;

public interface IProductoService {
    List<Producto> getAllProducts();
    Optional<Producto> getProduct(int id);
    Producto createProduct(ProductoDto productoDto);
    Producto updateProduct(int id, ProductoDto productoDto);
    void deleteProduct(int id);
}
