package com.service.Hotels.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;  
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
  private Long id;

  @Column(name = "price")
  private Float price;

  @Column(name = "available")
  private Boolean available;

  @Column(name = "description")
  private String description;

  @ManyToOne
  @JoinColumn(name = "hotel_id")
  @JsonIgnore
  private Hotel hotel;
}

