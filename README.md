
# Módulo 6 - Sesión 2
API REST Segura para Gestión de Productos y Usuarios

# Requerimientos mínimos
Para este proyecto, se utilizó Java 21 y SpringBoot. 
Las dependencias instaladas en 'pom.xml' son:
- jpa
- validation
- h2 database
- assertj
- mockito
- hamcrest
- spring security
- nimbusds
- spring security test

# Instrucciones para pruebas Postman

Dado que las rutas de autenticación se encuentran sin token: register y login, el usuario debe:
1) Registrarse en Postman: Método POST en http://localhost:8081/api/v1/register ingresando datos requeridos:
    Ejemplo: 
        { 
            "username": "username",
            "nombre": "nombre",
            "apellido": "apellido",
            "email": "email@email.com",
            "password": "password"
        }

2) El usuario creado tiene rol de usuario, por lo que no podrá acceder a rutas protegidas, como crear, modificar y eliminar productos. Debe cambiar el rol desde la base de datos. 
    a) En este caso h2 database: Acceder a la base de datos a través de: http://localhost:8081/h2-console.
    b) Realizar cambio del usuario registrado de la siguiente manera:

        UPDATE user_roles
        SET role = 'ROLE_ADMIN'
        WHERE user_id = (
            SELECT id FROM users WHERE username = 'anabasis' <= (aquí va nombre de username del usuario registrado)
        ); 
3) Login en Postman: Método POST en http://localhost:8081/api/v1/login
    Ingresar los datos requeridos 
        Ejemplo:
            {
                "username": "username",
                "password": "password"
            }

4) Ingresar el token de la respuesta que genera el login a Bearer Token que se encuentra en Auth dentro de la consulta. De esta forma, se podrá acceder a rutas protegidas que en este caso son:
    - Productos: Crear productos, modificar productos, listar todos los productos, listar productos activos, ver un producto por id, y cambiar estado de activo a pasivo y viceversa
    - Usuarios: Listar usuarios, ver un usuario particular, y cambiar estado de activo a pasivo y viceversa

# Equipo 9
Andrea Jiménez Espinoza
Francisca Robles Campano