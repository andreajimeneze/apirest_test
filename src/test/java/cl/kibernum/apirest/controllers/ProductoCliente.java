package cl.kibernum.apirest.controllers;

import static io.restassured.RestAssured.given;

import cl.kibernum.apirest.config.Specs;
import cl.kibernum.apirest.dto.ProductoDto;
import io.restassured.response.Response;

public class ProductoCliente {

    public Response listProduct() {
        return given()
                .spec(Specs.request())
                .when()
                .get("/api/v1/productos")
                .then()
                .spec(Specs.ok200())
                .extract().response();
    }

    public Response listActiveProducts() {
        return given()
                .spec(Specs.request())
                .when()
                .get("/api/v1/productos/activos")
                .then()
                .spec(Specs.ok200())
                .extract().response();
    }

    public Response createProduct(ProductoDto payload) {
        return given()
                .spec(Specs.request())
                .body(payload)
                .post("/api/v1/productos")
                .then()
                .spec(Specs.created201())
                .extract().response();
    }

    public Response getProduct(Long id) {
        return given()
                .spec(Specs.request())
                .pathParam("id", id)
                .when()
                .get("/api/v1/productos/{id}");
    }

    public Response updateProduct(Long id, ProductoDto payload) {
        return given()
                .spec(Specs.request())
                .body(payload)
                .pathParam("id", id)
                .when()
                .put("/api/v1/productos/{id}")
                .then()
                .spec(Specs.ok200())
                .extract().response();
    }

    public Response softDeleteproduct(Long id) {
        return given()
                .spec(Specs.request())
                .pathParam("id", id)
                .when()
                .patch("/api/v1/productos/{id}")
                .then()
                .extract().response();
    }
}
