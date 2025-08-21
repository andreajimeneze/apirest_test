package cl.kibernum.apirest.controllers;

import org.springframework.web.bind.annotation.RestController;

import cl.kibernum.apirest.dto.ProductoDto;
import cl.kibernum.apirest.entities.Producto;
import cl.kibernum.apirest.exception.ResourceNotFoundException;
import cl.kibernum.apirest.services.ProductoServiceImpl;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {
    private ProductoServiceImpl productoService;

    public ProductoController(ProductoServiceImpl productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<Producto> createProducto(@RequestBody ProductoDto productoDto) {
        Producto createdProducto = productoService.create(productoDto);
        URI location = URI.create(String.format("/api/v1/productos/%", createdProducto.getId()));
       // return new ResponseEntity<Producto>(createdProducto, HttpStatus.CREATED);
       return ResponseEntity.created(location).body(createdProducto);
    }

    @GetMapping
    public ResponseEntity<List<Producto>> getAllProducts() {
        return ResponseEntity.ok(productoService.getAll());
    }
    
    @GetMapping("/activos")
    public ResponseEntity<List<Producto>> getAllProductsActive() {
        return ResponseEntity.ok(productoService.findAllByActiveTrue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProduct(@PathVariable int id) {
        Producto producto = productoService.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        return ResponseEntity.ok(producto);
    
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProduct(@PathVariable int id, @Valid @RequestBody ProductoDto productoDto) {
        return ResponseEntity.ok(productoService.update(id, productoDto));
    }

    @PatchMapping("/{id}")
    public void softDeleteProducto(@PathVariable int id) {
       productoService.softDelete(id);
    }
}
