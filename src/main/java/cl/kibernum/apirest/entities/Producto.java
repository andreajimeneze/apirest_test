package cl.kibernum.apirest.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "productos")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true, nullable = false, length = 50)
    private String nombre;
    @Column(nullable = false, length = 200)
    private String descripcion;
    @Column(nullable = false)
    private int stock;
    @Column(nullable = false)
    private double precio;
    @Column(name = "status", nullable = false)
    private boolean active = true;
    
    public Producto(String nombre, String descripcion, int stock, double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stock = stock;
        this.precio = precio;
        this.active = true;
    }

    public Producto() { }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    public boolean isActive() {
        return active;
    }

    public void desactivateProduct() {
        this.active = false;
    }
    public void activateProduct() {
        this.active = true;
    }
}
