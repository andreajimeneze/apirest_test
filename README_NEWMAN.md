# Módulo 6 - Actividad sesión 4
Automatización de pruebas para API Rest en Plataforma de Comercio Electrónico

# Requerimientos mínimos
Postman 
Newman 6.2.1: npm install -g newman
dotenv 17.2.1: npm install dotenv

# Comando para automatizar pruebas con newman
En la terminal, ubicarse en la carpeta raíz del proyecto y ejecutar el comando:
    newman run postman/newman/suite_pruebas_api_2.json -e postman/newman/environment_pruebas_api_2.json --reporters cli

el cual permite entregar reporte desde terminal:

┌─────────────────────────┬─────────────────────┬────────────────────┐
│                         │            executed │             failed │
├─────────────────────────┼─────────────────────┼────────────────────┤
│              iterations │                   1 │                  0 │
├─────────────────────────┼─────────────────────┼────────────────────┤
│                requests │                   8 │                  0 │
├─────────────────────────┼─────────────────────┼────────────────────┤
│            test-scripts │                  16 │                  0 │
├─────────────────────────┼─────────────────────┼────────────────────┤
│      prerequest-scripts │                   8 │                  0 │
├─────────────────────────┼─────────────────────┼────────────────────┤
│              assertions │                  11 │                  0 │
├─────────────────────────┴─────────────────────┴────────────────────┤
│ total run duration: 3.3s                                           │
├────────────────────────────────────────────────────────────────────┤
│ total data received: 887B (approx)                                 │
├────────────────────────────────────────────────────────────────────┤
│ average response time: 283ms [min: 13ms, max: 1492ms, s.d.: 481ms] │
└────────────────────────────────────────────────────────────────────┘


# Nota
De manera excepcional para esta actividad, se dejaron abiertos los endpoints protegidos:

                .requestMatchers(HttpMethod.POST, "/api/v1/productos/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/productos/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/productos/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/productos/**").hasAnyRole("USER", "ADMIN")

Esto para permitir que las pruebas en Postman con Newman puedan correr de manera automatizada.


# Equipo 9
Andrea Jiménez Espinoza
Francisca Robles Campano