package cl.kibernum.apirest.security.domain;

public enum Role {
    ROLE_USER, // get /api/books/** modo lectura
    ROLE_ADMIN // post, put, delete /api/books/** modo escritura
}
