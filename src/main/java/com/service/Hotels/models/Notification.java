package com.service.Hotels.models;

import java.util.Date;

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

@Data
@Entity
@Table(name = "notifications")
public class Notification {
  public Notification() {
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "message")
  private String message;

  @Column(name = "date")
  private Date date;

  private Boolean isRead = false;

  // ---------------RelationShip-----------------//

  // Las notificaciones no llegaran hotel en sí. Sino que la recibira el usuario
  // que administra el hotel
  // Por lo tanto la relacion con el hotel no deberia.

  @ManyToOne
  @JoinColumn(name = "hotel_id")
  @JsonIgnore
  private Hotel hotel;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @JsonIgnore
  private User user;
}
