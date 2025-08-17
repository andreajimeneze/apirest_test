package cl.kibernum.apirest.security.dto;

import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {

     // Nombre de usuario a registrar. Obligatorio.
     @NotBlank
     private String username;

     // Contraseña en texto plano (se almacenará cifrada). Obligatoria.
     @NotBlank
     private String password;

    // Getters y setters estándar requeridos para la deserialización JSON.
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }


}
