package com.service.Hotels.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.Hotels.models.Hotel;
import com.service.Hotels.repositories.HotelRepository;
import com.service.Hotels.validation.HotelValidation;
import com.service.Hotels.exceptions.NotFoundException;
import com.service.Hotels.exceptions.BadRequestException;
import com.service.Hotels.interfaces.HotelService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {
    @Value("${google.api.key}")
    private String googleApiKey;
    @Autowired
    private HotelRepository hotelRepository;
    private RestTemplate restTemplate = new RestTemplate();

    public HotelServiceImpl(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @Override
    public List<Hotel> getAllHotelsByCity(String city) {
        // Verificar si ya existe información en la base de datos para la ciudad
        // especificada
        List<Hotel> hotelsInDatabase = hotelRepository.findAllByCity(city);
        if (!hotelsInDatabase.isEmpty()) {
            // Si ya hay información en la base de datos, retornarla directamente
            return hotelsInDatabase;
        } else {
            String apiKey = googleApiKey;
             //Validate the city
            if(!HotelValidation.isValidCity(city, googleApiKey)) throw new NotFoundException("Invalid city: " + city);
            
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
                        hotel.setCity(city.toLowerCase());

                        // Realizar solicitud adicional para obtener detalles del hotel y sus fotos
                        String placeId = result.path("place_id").asText();
                        String detailsUrl = "https://maps.googleapis.com/maps/api/place/details/json?place_id="
                                + placeId + "&fields=name,formatted_address,photos,formatted_phone_number,international_phone_number,rating&key="
                                + apiKey;
                        ResponseEntity<String> detailsResponse = restTemplate.getForEntity(detailsUrl, String.class);
                        if (detailsResponse.getStatusCode() == HttpStatus.OK) {
                            JsonNode detailsRoot = objectMapper.readTree(detailsResponse.getBody());
                            JsonNode photos = detailsRoot.path("result").path("photos");
                            String phone = detailsRoot.path("result").path("international_phone_number").toString();
                            Double rating = detailsRoot.path("result").path("rating").asDouble();
                            List<String> fotos = new ArrayList<>();
                            for (JsonNode photo : photos) {
                                fotos.add(
                                        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                                                + photo.path("photo_reference").asText() + "&key=" + apiKey);
                            }
                            hotel.setFotos(fotos);
                            hotel.setPhone(phone);
                            hotel.setFotos(fotos);
                            hotel.setRating(rating);
                        }

                        hotel.setPhone(result.path("formatted_phone_number").asText());
                        // Para añadir mas campos campos como email, description, etc.
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
                        () -> new NotFoundException("No hay ningun hotel con el ID: " + id));
        return hotel;
    }

    @Override
    public Hotel addHotel(Hotel newHotel) {
        try {
            Hotel hotel = hotelRepository.save(newHotel);
           return hotel;
        } catch (Exception e) {
            throw new BadRequestException("Error adding new Hotel : " + newHotel);
        }
    }

    @Override
    public void removeHotel(Long id) {
        if (id == null) {
            throw new BadRequestException("The ID cannot be null");
        }
        if (hotelRepository.existsById(id)) {
            try {
                hotelRepository.deleteById(id);
            } catch (IllegalArgumentException ex) {
                throw new BadRequestException("Error deleting the hotel with ID " + id);
            }
        }
    }
}
