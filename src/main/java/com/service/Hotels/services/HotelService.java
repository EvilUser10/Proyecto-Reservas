package com.service.Hotels.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.Hotels.models.Hotel;
import com.service.Hotels.models.Room;
import com.service.Hotels.repositories.HotelRepository;
import com.service.Hotels.validation.HotelValidation;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.service.Hotels.exceptions.NotFoundException;
import com.service.Hotels.assemblers.HotelModelAssembler;
import com.service.Hotels.controllers.HotelController;
import com.service.Hotels.exceptions.BadRequestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Comparator;

@Service
public class HotelService {
    @Value("${google.api.key}")
    private String googleApiKey;
    @Autowired
    private HotelRepository hotelRepository;

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private HotelModelAssembler assembler;

    public CollectionModel<EntityModel<Hotel>> getAllHotels(Integer max, String orderby, String city) {

        // List<Hotel> hotels = hotelRepository.findAll().stream()
        //         .filter(hotel -> city == null || hotel.getCity().equalsIgnoreCase(city))
        //         .limit(max)
        //         .collect(Collectors.toList());

                List<Hotel> hotels = hotelRepository.findAll().stream()
                .filter(hotel -> city == null || hotel.getCity().equalsIgnoreCase(city))
                .collect(Collectors.toList());

                Collections.shuffle(hotels);


        if (orderby.equalsIgnoreCase("rating")) {
            hotels.sort(Comparator.comparing(Hotel::getRating).reversed());
        } else if (orderby.equalsIgnoreCase("name")) {
            hotels.sort(Comparator.comparing(Hotel::getName));
        } else if (orderby.equalsIgnoreCase("random")) {
            // Ordenar aleatoriamente (manteniendo el orden actual)
            Collections.shuffle(hotels);
        }
        
        if (hotels.isEmpty()) {
            throw new NotFoundException("No se encuentra ningún hotel con los criterios indicados");
        }
        hotels = hotels.stream().limit(max).collect(Collectors.toList());
        Object userLoggedIn = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<EntityModel<Hotel>> hotelModels = hotels.stream()
                .map(hotel -> {
                    // Excluir las propiedades bookings y notifications si el usuario no es un hotel
                    if (userLoggedIn instanceof String && userLoggedIn.equals("anonymousUser")) {
                        hotel.setBookings(null);
                        hotel.setNotifications(null);
                    }
                    return assembler.toModel(hotel);
                })
                .collect(Collectors.toList());

        return CollectionModel.of(hotelModels,
                // linkTo(methodOn(HotelController.class).getAll(max, orderby,
                // city)).withSelfRel());
                linkTo(methodOn(HotelController.class).getAll(city)).withSelfRel());
    }

    public CollectionModel<EntityModel<Hotel>> getAllHotelsByCity(String city) {
        // Verificar si ya existe información en la base de datos para la ciudad
        // especificada
        List<Hotel> hotels = hotelRepository.findAllByCity(city);
        if (hotels.isEmpty()) {
            String apiKey = googleApiKey;
            // Validate the city
            if (!HotelValidation.isValidCity(city, googleApiKey))
                throw new NotFoundException("Invalid city: " + city);

            String searchUrl = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=hotel+in+" + city
                    + "&key=" + apiKey;
            ResponseEntity<String> response = restTemplate.getForEntity(searchUrl, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    JsonNode root = objectMapper.readTree(response.getBody());
                    JsonNode results = root.path("results");
                    for (JsonNode result : results) {
                        Hotel hotel = new Hotel();
                        hotel.setName(result.path("name").asText());
                        hotel.setAddress(result.path("formatted_address").asText());
                        hotel.setCity(city.toLowerCase());
                        hotel.setLatitud(result.path("geometry").path("location").path("lat").asDouble());
                        hotel.setLongitud(result.path("geometry").path("location").path("lng").asDouble());
                        hotel.setRating(result.path("rating").asDouble());
                        ;
                        // Realizar solicitud adicional para obtener detalles del hotel y sus fotos
                        String placeId = result.path("place_id").asText();
                        String detailsUrl = "https://maps.googleapis.com/maps/api/place/details/json?place_id="
                                + placeId
                                + "&fields=name,formatted_address,photos,formatted_phone_number,international_phone_number,rating&key="
                                + apiKey;
                        ResponseEntity<String> detailsResponse = restTemplate.getForEntity(detailsUrl, String.class);
                        if (detailsResponse.getStatusCode() == HttpStatus.OK) {
                            JsonNode detailsRoot = objectMapper.readTree(detailsResponse.getBody());
                            JsonNode photos = detailsRoot.path("result").path("photos");
                            String phone = detailsRoot.path("result").path("international_phone_number").toString();
                            List<String> fotos = new ArrayList<>();
                            for (JsonNode photo : photos) {
                                fotos.add(
                                        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
                                                + photo.path("photo_reference").asText() + "&key=" + apiKey);
                            }
                            hotel.setFotos(fotos);
                            hotel.setPhone(phone);
                            hotel.setFotos(fotos);
                            List<Room> rooms = createRandomRooms(hotel);

                            for (Room room : rooms) {
                                room.setHotel(hotel);
                            }
                            // Random random = new Random();
                            // int index = random.nextInt(rooms.size());
                            hotel.setDescription(generateRandomDescription(hotel.getName(), "hotel"));
                            ;
                            hotel.setRooms(rooms);
                            hotelRepository.save(hotel);
                        }

                        hotels.add(hotel);
                        // Save rooms en database
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // Manejar la excepción apropiadamente
                }
            }
            else hotels = Collections.emptyList();
        }

        Object userLoggedIn = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<EntityModel<Hotel>> hotelModels = hotels.stream()
                .map(hotel -> {
                    // Excluir las propiedades bookings y notifications si el usuario no es un hotel
                    if (userLoggedIn instanceof String && userLoggedIn.equals("anonymousUser")) {
                        hotel.setBookings(null);
                        hotel.setNotifications(null);
                    }
                    return assembler.toModel(hotel);
                })
                .collect(Collectors.toList());
        return CollectionModel.of(hotelModels, linkTo(methodOn(HotelController.class).getAll(city)).withSelfRel());
    }

    public Hotel updateHotel(Long id, Hotel newHotel) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No hay ningun hotel con el ID: " + id));
        hotel.setAddress(newHotel.getAddress() == null ? hotel.getAddress() : newHotel.getAddress());
        hotel.setCity(newHotel.getCity() == null ? hotel.getCity() : newHotel.getCity());
        hotel.setDescription(newHotel.getDescription() == null ? hotel.getDescription() : newHotel.getDescription());
        hotel.setEmail(newHotel.getEmail() == null ? hotel.getEmail() : newHotel.getEmail());
        hotel.setPhone(newHotel.getPhone() == null ? hotel.getPhone() : newHotel.getPhone());
        hotel.setFotos(newHotel.getFotos() == null ? hotel.getFotos() : newHotel.getFotos());
        hotel.setName(newHotel.getName());
        this.addHotel(hotel);
        return hotel;
    }

    public Hotel findHotelById(Long id) {
        return hotelRepository.findById(id).get();
    }

    public Hotel addHotel(Hotel newHotel) {
        try {
            Hotel hotel = hotelRepository.save(newHotel);
            return hotel;
        } catch (Exception e) {
            throw new BadRequestException("Error adding new Hotel : " + newHotel);
        }
    }

    public CollectionModel<EntityModel<Hotel>> getHotel(String city, Long id) {
        //Object userLoggedIn = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Obtener la lista de hoteles en la ciudad dada
        if (city == null || id == null) {
            throw new BadRequestException("City and Id can not be null");
        }
        CollectionModel<EntityModel<Hotel>> hotels = this.getAllHotelsByCity(city);
        
        return hotels;

        // Buscar el hotel específico por su ID
        // Hotel hotelModel = hotels.stream()
        //         .map(hotel -> {
        //             if (userLoggedIn instanceof String && userLoggedIn.equals("anonymousUser")) {
        //                 hotel.setBookings(null);
        //                 hotel.setNotifications(null);
        //             }
        //             return hotel;
        //         })
        //         .filter(h -> h.getId().equals(id))
        //         .findFirst()
        //         .orElseThrow(() -> new NotFoundException(
        //                 "No se encuentra ningún hotel en la ciudad de " + city + " con el ID " + id));

        // // Convertir el hotel encontrado en un EntityModel
        // return assembler.toModel(hotelModel);

    }

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
            room.setDescription(generateRandomDescription(hotel.getName(), "room"));
            rooms.add(room);
        }

        return rooms;
    }

    // Método para generar un precio aleatorio para una habitación
    private static float generateRandomPrice() {
        Random random = new Random();
        float minPrice = 50.0f; // Precio mínimo
        float maxPrice = 500.0f; // Precio máximo
        float randomPrice = minPrice + random.nextFloat() * (maxPrice - minPrice);
        return Math.round(randomPrice * 100.0) / 100.0f;
    }

    // Método para generar disponibilidad aleatoria para una habitación
    private static boolean generateRandomAvailability() {
        Random random = new Random();
        return random.nextBoolean();
    }

    // Método para generar una descripción aleatoria para una habitación
    private static String generateRandomDescription(String hotelName, String modelType) {
        String[] hotelDescriptions = {
                "Hotel " + hotelName + " te ofrece una experiencia única en el corazón de la ciudad. Precio: "
                        + " por noche.",
                "Descubre el encanto y la elegancia de " + hotelName + " hotel. Precio: " + " por noche.",
                "Disfruta de la hospitalidad y el confort en " + hotelName + " hotel. Precio: " + " por noche.",
                "Experimenta la magia de " + hotelName + ", donde cada detalle está pensado para tu comodidad. Precio: "
                        + " por noche.",
                "Sumérgete en el lujo y la tranquilidad de " + hotelName + ". Precio: " + " por noche.",
                "Déjate seducir por la atmósfera única de " + hotelName + ". Precio: " + " por noche.",
                "Relájate y rejuvenece en " + hotelName + ", tu refugio en la ciudad. Precio: " + " por noche.",
                "Embárcate en una aventura única en " + hotelName + ", donde cada rincón cuenta una historia. Precio: "
                        + " por noche.",
                "Descubre un mundo de posibilidades en " + hotelName + ", donde tus sueños se hacen realidad. Precio: "
                        + " por noche.",
                "Siente la esencia de " + hotelName + ", donde la elegancia se fusiona con el encanto local. Precio: "
                        + " por noche."
        };

        String[] roomDescriptions = {
                "Habitación con vistas panorámicas y comodidades modernas. Precio: " + " por noche.",
                "Suite exclusiva con terraza privada y jacuzzi. Precio: " + " por noche.",
                "Acogedora habitación con detalles rústicos y vistas al jardín. Precio: " + " por noche.",
                "Alojamiento espacioso con zona de estar y decoración elegante. Precio: " + " por noche.",
                "Suite junior con vistas al mar y servicios de lujo. Precio: " + " por noche.",
                "Habitación familiar con espacios separados y entretenimiento para niños. Precio: " + " por noche.",
                "Suite de lujo con chimenea y servicios de conserjería personalizados. Precio: " + " por noche.",
                "Habitación ejecutiva con escritorio y conexión Wi-Fi de alta velocidad. Precio: " + " por noche.",
                "Suite premium con terraza privada y servicio de habitaciones las 24 horas. Precio: " + " por noche.",
                "Habitación temática diseñada para una experiencia única y memorables. Precio: " + " por noche."
        };

        String[] descriptionsToUse = modelType.equals("hotel") ? hotelDescriptions : roomDescriptions;

        Random random = new Random();
        int index = random.nextInt(roomDescriptions.length);
        return descriptionsToUse[index];
    }

}
