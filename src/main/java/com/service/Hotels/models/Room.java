package com.service.Hotels.models;

//import org.hibernate.mapping.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import jakarta.persistence.Column;  
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Entity
@Getter
@Setter
@Table(name = "rooms")
public class Room {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  private Long id;

  @Column(name = "type")
  private String type;

  @Column(name = "price")
  private Float price;

  @Column(name = "available")
  private Boolean available;

  @Column(name = "photos")
  private List<String> photos;

  @Column(name = "description")
  private String description;
}

