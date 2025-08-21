package cl.kibernum.apirest.security.dto;

import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {

    // Nombre de usuario a registrar. Obligatorio.
    @NotBlank
    private String username;
    @NotBlank
    private String nombre;
    @NotBlank
    private String apellido;
    @NotBlank
    private String email;

    // Contraseña en texto plano (se almacenará cifrada). Obligatoria.
    @NotBlank
    private String password;

    // Getters y setters estándar requeridos para la deserialización JSON.
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

        public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
