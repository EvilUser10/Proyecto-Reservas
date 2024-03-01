package com.service.Hotels.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "payment")
public class Payment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

  @Column(name = "cuantity")
  private Double cuantity;

  @Column(name = "payment_method")
  private String paymentMethod;

  @Column(name = "date")
  private Date date;

  @Column(name = "state")
  private String state;
}
