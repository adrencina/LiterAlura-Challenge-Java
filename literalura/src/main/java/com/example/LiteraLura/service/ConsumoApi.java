package com.example.LiteraLura.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoApi {

    // Método para realizar la petición GET a la API de Gutendex
    public String obtenerDatos(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = null;
        try {
            // Enviamos la solicitud y manejamos la respuesta como String
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            // Si algo falla en la conexión, lanzamos una excepción con el detalle
            throw new RuntimeException("Error al conectar con la API: " + e.getMessage());
        }

        return response.body();
    }
}
