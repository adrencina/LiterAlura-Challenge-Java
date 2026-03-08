package com.example.LiteraLura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvierteDatos implements IConvierteDatos {
    // Instanciamos el ObjectMapper de Jackson para mapear el JSON
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            // Intentamos convertir el String JSON en la clase que pasemos por parámetro
            return objectMapper.readValue(json, clase);
        } catch (JsonProcessingException e) {
            // Error en el mapeo de los campos (posiblemente falta una anotación @JsonAlias)
            throw new RuntimeException("Error al deserializar el JSON: " + e.getMessage());
        }
    }
}
