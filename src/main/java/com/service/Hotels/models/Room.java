package com.service.Hotels.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

//import org.hibernate.mapping.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import jakarta.persistence.Column;  
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
  @JsonIgnore
  private Long id;

  @Column(name = "price")
  private Float price;

  @Column(name = "available")
  private Boolean available;

  @Column(name = "description")
  private String description;

  @ManyToOne(fetch = FetchType.EAGER)
  //@JsonBackReference
  @JoinColumn(name = "hotel_id")
  private Hotel hotel;
}

