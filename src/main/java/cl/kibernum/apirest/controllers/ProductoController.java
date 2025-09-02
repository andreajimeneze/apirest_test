package cl.kibernum.apirest.controllers;

import cl.kibernum.apirest.dto.ProductoDto;
import cl.kibernum.apirest.entities.Producto;
import cl.kibernum.apirest.exception.ResourceNotFoundException;
import cl.kibernum.apirest.services.ProductoServiceImpl;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoServiceImpl productoService;

    public ProductoController(ProductoServiceImpl productoService) {
        this.productoService = productoService;
    }

    @PostMapping
    public ResponseEntity<Producto> createProducto(@Valid @RequestBody ProductoDto productoDto) {
        Producto createdProducto = productoService.create(productoDto);
        return new ResponseEntity<>(createdProducto, HttpStatus.CREATED);
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
    public ResponseEntity<Producto> getProduct(@PathVariable Long id) {
        Producto producto = productoService.getById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        return ResponseEntity.ok(producto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductoDto productoDto) {
        return ResponseEntity.ok(productoService.update(id, productoDto));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> softDeleteProducto(@PathVariable Long id) {
        productoService.softDelete(id);
        return ResponseEntity.accepted().build();
    }
}

