package cl.kibernum.apirest.test;

import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cl.kibernum.apirest.controllers.ProductoCliente;
import cl.kibernum.apirest.dto.ProductoDto;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ProductoCrudTest {
    private static ProductoCliente producto;
    private Long createdId;

    @BeforeAll
    static void setup() {
        producto = new ProductoCliente();
    }

    @Test
    @DisplayName("GET / productos - lista todos los productos")
    void listProducts_ok() {
        Response response = producto.listProduct();
        JsonPath json = response.jsonPath();
        List<Map<String, Object>> productos = json.getList("$");
        Assertions.assertThat(productos)
                .as("La lista de productos no debe estar vacía")
                .isNotEmpty();

        Assertions.assertThat(response.getHeader("Content-Type")).contains("application/json");
    }

    @Test
    @DisplayName("GET / productos - lista todos los productos activos")
    void listActiveProducts_ok() {
        Response response = producto.listActiveProducts();
        JsonPath json = response.jsonPath();
        List<Map<String, Object>> productosActivos = json.getList("$");
        Assertions.assertThat(productosActivos)
                .as("La lista de productos activos no debe estar vacía")
                .isNotEmpty();

        Assertions.assertThat(response.getHeader("Content-Type")).contains("application/json");
    }

    @Test
    @DisplayName("POST /productos - crear un producto")
    void createProduct() {
        String nombre = "Notebook";
        String descripcion = "Huawei D14";
        int stock = 20;
        double precio = 600000;

        ProductoDto payload = new ProductoDto(nombre, descripcion, stock, precio);

        Response response = producto.createProduct(payload);
        JsonPath json = response.jsonPath();
        createdId = json.getLong("id");
        Assertions.assertThat(createdId).isNotNull();
        Assertions.assertThat(json.getString("nombre")).isEqualTo(nombre);
        Assertions.assertThat(json.getString("descripcion")).isEqualTo(descripcion);
        Assertions.assertThat(json.getInt("stock")).isEqualTo(stock);
        Assertions.assertThat(json.getDouble("precio")).isEqualTo(precio);
    }

    @Test
    @DisplayName("GET /productos/{id} - obtiene un producto existente")
    void getProductById() {
        Response response = producto.getProduct(1L);
        JsonPath json = response.jsonPath();
        Assertions.assertThat(json.getInt("id")).isEqualTo(1L);
        Assertions.assertThat(json.getString("nombre")).isNotBlank();
    }

    @Test
    @DisplayName("PUT /productos/{id} - actualizar un producto")
    void updateProducto_ok() {
        Long idToUpdate = 1L;
        String nombre = "Notebook Huawei";
        String descripcion = "Memoria RAM 8Gb, 500Gb";
        int stock = 18;
        double precio = 550000;

        ProductoDto payload = new ProductoDto(nombre, descripcion, stock, precio);

        Response response = producto.updateProduct(idToUpdate, payload);
        JsonPath json = response.jsonPath();

        Assertions.assertThat(json.getString("nombre")).isEqualTo(nombre);
        Assertions.assertThat(json.getString("descripcion")).isEqualTo(descripcion);
        Assertions.assertThat(json.getInt("stock")).isEqualTo(stock);
        Assertions.assertThat(json.getDouble("precio")).isEqualTo(precio);
    }

    @Test
    @DisplayName("PATCH /productos/{id} - cambiar estado a inactivo (softDelete)")
    void deleteProduct_ok() {
        Long idToDelete = (createdId != null && createdId > 0) ? createdId : 1L;

        Response response = producto.softDeleteproduct(idToDelete);
        int status = response.statusCode();

        Assertions.assertThat(status).isIn(200, 202);
    }
}
