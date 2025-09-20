# Requisitos

Node.js ≥ 14
Newman 6.2.1: npm install -g newman
dotenv 17.2.1: npm install dotenv
Newman HTML Reporter (opcional, para generar reportes en HTML):
npm install -g newman-reporter-html

# Comando para ejecutar las pruebas

- Ejecuta toda la colección:
    newman run postman/newman/ApirestModular6.postman_collection.json -e postman/newman/APIEnvironment.postman_environment.json --reporters cli

# Estructura de carpetas

apirestAssured/
│
├─ postman/
│   ├─ newman/
│   │   ├─ ApirestModular6.postman_collection.json
│   │   └─ APIEnvironment.postman_environment.json
│
├─ reports/        # Carpeta donde se guardan los reportes HTML

# Salida en consola al ejecutar el test

ApirestModular6

→ GET listado productos
  GET http://localhost:8081/api/v1/productos [200 OK, 667B, 869ms]

→ POST crear producto
  POST http://localhost:8081/api/v1/productos [201 Created, 265B, 357ms]
  √  201 Created
  √  El ID del producto se setea en variable de entorno

→ PUT modificar producto
  PUT http://localhost:8081/api/v1/productos/5 [200 OK, 259B, 67ms]
  √  200 OK
  √  Producto actualizado
  √  Stock actualizado correctamente

→ Soft delete producto
  PATCH http://localhost:8081/api/v1/productos/5 [202 Accepted, 129B, 18ms]
  √  202 aceptado

→ ver producto por id
  GET http://localhost:8081/api/v1/productos/5 [200 OK, 260B, 13ms]
  √  200 OK
  √  Producto corresponde al ID

→ GET listado productos ACTIVOS
  GET http://localhost:8081/api/v1/productos/activos [200 OK, 667B, 14ms]  √  200 OK
  √  Número de productos activos: 4

┌─────────────────────────┬────────────────────┬────────────────────┐    
│                         │           executed │             failed │    
├─────────────────────────┼────────────────────┼────────────────────┤    
│              iterations │                  1 │                  0 │    
├─────────────────────────┼────────────────────┼────────────────────┤    
│                requests │                  6 │                  0 │    
├─────────────────────────┼────────────────────┼────────────────────┤    
│            test-scripts │                  6 │                  0 │    
├─────────────────────────┼────────────────────┼────────────────────┤    
│      prerequest-scripts │                  0 │                  0 │    
├─────────────────────────┼────────────────────┼────────────────────┤    
│              assertions │                 10 │                  0 │    
├─────────────────────────┴────────────────────┴────────────────────┤    
│ total run duration: 2s                                            │    
├───────────────────────────────────────────────────────────────────┤    
│ total data received: 1.29kB (approx)                              │    
├───────────────────────────────────────────────────────────────────┤    
│ average response time: 223ms [min: 13ms, max: 869ms, s.d.: 313ms] │    
├─────────────────────────┼────────────────────┼────────────────────┤    
│                requests │                  6 │                  0 │    
├─────────────────────────┼────────────────────┼────────────────────┤    
│            test-scripts │                  6 │                  0 │    
├─────────────────────────┼────────────────────┼────────────────────┤    
│      prerequest-scripts │                  0 │                  0 │    
├─────────────────────────┼────────────────────┼────────────────────┤    
│              assertions │                 10 │                  0 │    
├─────────────────────────┴────────────────────┴────────────────────┤    
│ total run duration: 2s                                            │    
├───────────────────────────────────────────────────────────────────┤    
│ total data received: 1.29kB (approx)                              │    
├───────────────────────────────────────────────────────────────────┤    
│ average response time: 223ms [min: 13ms, max: 869ms, s.d.: 313ms] │    
│      prerequest-scripts │                  0 │                  0 │    
├─────────────────────────┼────────────────────┼────────────────────┤    
│              assertions │                 10 │                  0 │    
├─────────────────────────┴────────────────────┴────────────────────┤    
│ total run duration: 2s                                            │    
├───────────────────────────────────────────────────────────────────┤    
│ total data received: 1.29kB (approx)                              │    
├───────────────────────────────────────────────────────────────────┤    
│ average response time: 223ms [min: 13ms, max: 869ms, s.d.: 313ms] │    
├───────────────────────────────────────────────────────────────────┤    
│ average response time: 223ms [min: 13ms, max: 869ms, s.d.: 313ms] │    
│ average response time: 223ms [min: 13ms, max: 869ms, s.d.: 313ms] │    
└───────────────────────────────────────────────────────────────────┘    

