package cl.kibernum.apirest.security.domain;

// Anotaciones JPA para mapear colecciones y tablas secundarias.
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users")
public class UserAccount {

     // Identificador primario autogenerado por la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     // Nombre de usuario único y obligatorio, limitado a 100 caracteres.
    @Column(unique = true, nullable = false, length = 100)
    private String username;

     // Hash de la contraseña (BCrypt). Nunca guardar contraseñas en texto plano.
    @Column(nullable = false)
    private String password; // BCrypt hashed

     // Flag de habilitación de la cuenta (true = activa, false = deshabilitada/bloqueada).
    @Column(nullable = false)
    private boolean enabled = true;

     // Versión del token. Si se incrementa, invalida los refresh tokens existentes.
    @Column(nullable = false)
    private int tokenVersion = 0; // used to invalidate refresh tokens

     // Colección de roles del usuario. Se persiste en tabla separada user_roles (user_id, role).
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 30)
    private Set<Role> roles = new HashSet<>();

     // Getters y setters estándar.
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public int getTokenVersion() { return tokenVersion; }
    public void setTokenVersion(int tokenVersion) { this.tokenVersion = tokenVersion; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    /**
      * Incrementa la versión de token para invalidar refresh tokens previamente emitidos.
      * Útil para forzar cierre de sesión global del usuario (p. ej., al cambiar contraseña).
     */
    public void incrementTokenVersion() {
        this.tokenVersion++;
    }
}
