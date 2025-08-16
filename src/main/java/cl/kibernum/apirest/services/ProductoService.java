package cl.kibernum.apirest.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.kibernum.apirest.dto.ProductoDto;
import cl.kibernum.apirest.models.Producto;
import cl.kibernum.apirest.repositories.ProductoRepository;
import cl.kibernum.apirest.exception.ResourceNotFoundException;

@Service
public class ProductoService implements IProductoService {
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> getAllProducts() {
       return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> getProduct(int id) {
       return productoRepository.findById(id);
    }

    @Override
    public Producto createProduct(ProductoDto productoDto) {
        Producto producto = new Producto();

        producto.setNombre(productoDto.getNombre());
        producto.setDescripcion(productoDto.getDescripcion());
        producto.setStock(productoDto.getStock());
        producto.setPrecio(productoDto.getPrecio());
        return productoRepository.save(producto);
    }

    @Override
    @Transactional
    public Producto updateProduct(int id, ProductoDto productoDto) {
       Producto searchingProducto = productoRepository.findById(id)
                                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
      
            searchingProducto.setNombre(productoDto.getNombre());
            searchingProducto.setDescripcion(productoDto.getDescripcion());
            searchingProducto.setStock(productoDto.getStock());
            searchingProducto.setPrecio(productoDto.getPrecio());
        
        return productoRepository.save(searchingProducto);
    }

    @Override
    @Transactional
    public void deleteProduct(int id) {
       Producto searchingProducto = productoRepository.findById(id)
                                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
      
        productoRepository.delete(searchingProducto);
    }
}
