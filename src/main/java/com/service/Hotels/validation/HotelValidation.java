package com.service.Hotels.validation;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LocationType;

import java.io.IOException;


public class HotelValidation {
    public static boolean isValidCity(String cityName, String apiKey) {
        // Inicializar el contexto de la API con tu clave de API de Google Places
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();

        try {
            // Realizar la geocodificación para obtener la información sobre la ciudad
            GeocodingResult[] results = GeocodingApi.geocode(context, cityName).await();

            // Verificar si se encontró una ciudad y si la ubicación es de tipo "locality" (ciudad)
            if (results.length > 0 && results[0].geometry != null &&
                    results[0].geometry.locationType == LocationType.APPROXIMATE) {
                return true; // La ciudad es válida
            } else {
                return false; // La ciudad no es válida
            }
        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
            return false; // Error al llamar a la API
        }
    }

}
