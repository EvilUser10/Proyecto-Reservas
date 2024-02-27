package com.service.Hotels.Model;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(includeFieldNames=true)
@Entity
@Table(name = "users")
public class User {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@NonNull private Long id;
	
	@Column(name = "name")
	@NonNull private String name;

	@Column(name = "surname")
	private String surname;

    @Column(name = "email")
    @NonNull private String email;

	@Column(name = "password")
	@NonNull private String password;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Reserve> reserves;
}
