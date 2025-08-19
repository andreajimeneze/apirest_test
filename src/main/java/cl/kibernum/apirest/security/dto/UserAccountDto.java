package cl.kibernum.apirest.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserAccountDto {
    @NotBlank
    @Size(max = 50)
    private String nombre;
    @NotBlank
    @Size(max = 50)
    private String apellido;
    @NotBlank
    @Size(max = 50)
    private String email;

    public UserAccountDto(String nombre, String apellido, String email) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }
}
