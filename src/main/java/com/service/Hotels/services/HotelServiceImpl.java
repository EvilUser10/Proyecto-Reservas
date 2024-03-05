package com.service.Hotels.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.Hotels.models.Hotel;
import com.service.Hotels.repositories.HotelRepository;
import com.service.Hotels.exceptions.NotFoundException;
import com.service.Hotels.interfaces.HotelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//import java.util.Optional;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;
    private RestTemplate restTemplate = new RestTemplate();

    public HotelServiceImpl(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public List<Hotel> getAllHotelsByCity(String city) {
        // Verificar si ya existe información en la base de datos para la ciudad especificada
        List<Hotel> hotelsInDatabase = hotelRepository.findAllByCity(city);
        if (!hotelsInDatabase.isEmpty()) {
            // Si ya hay información en la base de datos, retornarla directamente
            return hotelsInDatabase;
        } else {
            String apiKey = "AIzaSyAP09NW2h_iD78I82_cDBdE0k50d2peE98";
            String searchUrl = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=hotel+in+" + city
                    + "&key=" + apiKey;
            ResponseEntity<String> response = restTemplate.getForEntity(searchUrl, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonNode root = objectMapper.readTree(response.getBody());
                    JsonNode results = root.path("results");
                    List<Hotel> hotels = new ArrayList<>();
                    for (JsonNode result : results) {
                        Hotel hotel = new Hotel();
                        hotel.setName(result.path("name").asText());
                        hotel.setAddress(result.path("formatted_address").asText());
                        hotel.setCity(city);

                        // Realizar solicitud adicional para obtener detalles del hotel y sus fotos
                        String placeId = result.path("place_id").asText();
                        String detailsUrl = "https://maps.googleapis.com/maps/api/place/details/json?place_id="
                                + placeId + "&fields=name,formatted_address,photos,formatted_phone_number&key=" + apiKey;
                        ResponseEntity<String> detailsResponse = restTemplate.getForEntity(detailsUrl, String.class);
                        if (detailsResponse.getStatusCode() == HttpStatus.OK) {
                            JsonNode detailsRoot = objectMapper.readTree(detailsResponse.getBody());
                            JsonNode photos = detailsRoot.path("result").path("photos");
                            List<String> fotos = new ArrayList<>();
                            for (JsonNode photo : photos) {
                                fotos.add(
                                        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                                                + photo.path("photo_reference").asText() + "&key=" + apiKey);
                            }
                            hotel.setFotos(fotos);
                        }

                        hotel.setPhone(result.path("formatted_phone_number").asText());
                        //Para añadir mas campos campos como email, description, etc.
                        hotels.add(hotel);
                    }
                    // Guardar los hoteles encontrados en la base de datos
                    hotelRepository.saveAll(hotels);
                    return hotels;
                } catch (Exception e) {
                    e.printStackTrace();
                    // Manejar la excepción apropiadamente
                }
            }
            return Collections.emptyList();
        }
    }

    @Override
    public Hotel findHotelById(Long id) {

        Hotel hotel = hotelRepository.findById(id)
        .orElseThrow(
            () -> new NotFoundException("No hay ningun hotel con el ID: " + id)
        );

        return hotel;
    }


}
