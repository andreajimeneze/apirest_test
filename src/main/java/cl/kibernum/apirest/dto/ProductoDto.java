package cl.kibernum.apirest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class ProductoDto {
    @NotBlank
    @Size(max = 50)
    private String nombre;
    @NotBlank
    @Size(max = 50)
    private String descripcion;
   @NotNull
    private int stock;
    @NotNull
    private double precio;
    @NotNull
    private boolean active = true;
    
    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getStock() {
        return stock;
    }

    public double getPrecio() {
        return precio;
    }

    public boolean isActive() {
        return active;
    }  
}
