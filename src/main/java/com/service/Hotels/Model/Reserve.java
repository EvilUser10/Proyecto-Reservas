package com.service.Hotels.Model;

import java.sql.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames=true)
@Entity
@Table(name = "reserves")
public class Reserve {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "finish_date")
	private Date finishDate;

    @Column(name = "state")
    private String state;

	@Column(name = "password")
	private String password;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
