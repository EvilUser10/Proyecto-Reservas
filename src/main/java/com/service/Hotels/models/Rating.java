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
@Table(name = "ratings")
public class Rating {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "rating")
  private int rating;

  @Column(name = "comment")
  private String comment;

  @ManyToOne
  @JoinColumn(name = "hotel_id")
  @JsonIgnore
  private Hotel hotel;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @JsonIgnore
  private User user;

}
