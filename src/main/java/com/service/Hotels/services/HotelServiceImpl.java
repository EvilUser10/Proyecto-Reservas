package com.service.Hotels.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.Hotels.models.Hotel;
import com.service.Hotels.models.Room;
import com.service.Hotels.repositories.HotelRepository;
import com.service.Hotels.repositories.MyRoomRepository;
import com.service.Hotels.validation.HotelValidation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    @Autowired
    private MyRoomRepository roomRepository;
    private RestTemplate restTemplate = new RestTemplate();

    public HotelServiceImpl(HotelRepository hotelRepository, MyRoomRepository roomRepository) {
        this.hotelRepository = hotelRepository;
        this.roomRepository = roomRepository;
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
                            List<Room> rooms = createRandomRooms(hotel);
                            for (Room room : rooms) {
                                hotel.addRooms(room);
                            }
                            hotelRepository.save(hotel);
                            roomRepository.saveAll(rooms);
                        }

                        
                        hotels.add(hotel);
                        //Save rooms en database
                        
                    }
                    // Guardar los hoteles encontrados en la base de datos
                    //hotelRepository.saveAll(hotels);
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


    private static List<Room> createRandomRooms(Hotel hotel) {
        List<Room> rooms = new ArrayList<>();
        Random random = new Random();
        int numberOfRooms = random.nextInt(8) + 3; // Entre 3 y 10 habitaciones


        for (int i = 0; i < numberOfRooms; i++) {
            Room room = new Room();
            room.setPrice(generateRandomPrice());
            room.setAvailable(generateRandomAvailability());
            room.setDescription(generateRandomDescription(hotel.getName(), room.getPrice()));
            rooms.add(room);
        }

        return rooms;
    }

    // Método para generar un precio aleatorio para una habitación
    private static float generateRandomPrice() {
        Random random = new Random();
        float minPrice = 50.0f; // Precio mínimo
        float maxPrice = 500.0f; // Precio máximo
        return minPrice + random.nextFloat() * (maxPrice - minPrice);
    }

    // Método para generar disponibilidad aleatoria para una habitación
    private static boolean generateRandomAvailability() {
        Random random = new Random();
        return random.nextBoolean();
    }

    // Método para generar una descripción aleatoria para una habitación
    private static String generateRandomDescription(String hotelName, float price) {
        String[] descriptions = {
            "Habitación cómoda con vistas espectaculares a la ciudad. Precio: $" + price + " por noche.",
            "Suite de lujo con todas las comodidades necesarias para una estancia relajante. Precio: $" + price + " por noche.",
            "Habitación acogedora con decoración moderna y ambiente tranquilo. Precio: $" + price + " por noche.",
            "Alojamiento espacioso y elegante con todas las comodidades necesarias. Precio: $" + price + " por noche."
        };
        Random random = new Random();
        int index = random.nextInt(descriptions.length);
        return descriptions[index];
    }
}
