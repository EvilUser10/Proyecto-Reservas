package com.service.Hotels.models;

import java.util.Date;

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
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "message")
  private String message;

  @Column(name = "date")
  private Date date;

  @ManyToOne
  @JoinColumn(name = "hotel_id")
  private Hotel hotel;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
