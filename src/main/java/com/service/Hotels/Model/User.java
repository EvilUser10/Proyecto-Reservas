package com.service.Hotels.Model;

import org.springframework.data.annotation.CreatedDate;


import java.sql.Date;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


public class User {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name")
	private String name;

    @Column(name = "password")
	private String password;

    @Column(name = "email")
    private String email;
	
	@Column(name = "create_time")
	@CreatedDate
	private Date date;
	
    public User(){

    }

	public User(Long id, String name, String password, String email,  Date date) {
		this.id = id;
		this.name = name;
        this.password = password;
		this.email = email;
		this.date = date;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", date=" + date + "]";
	}
}
