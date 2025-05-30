package com.cts.entities;

import jakarta.persistence.Column; // Import Column
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint; // Import UniqueConstraint
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address", uniqueConstraints = { // Add uniqueConstraints
    @UniqueConstraint(columnNames = {"house_no", "street", "landmark", "city", "state", "pin_code"}) // Define unique columns
})
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// Use @Column to explicitly define column names if they differ from field names
	@Column(name = "house_no")
	private String houseNo;

	@Column(name = "street")
	private String street;

	@Column(name = "landmark")
	private String landmark;

	@Column(name = "city")
	private String city;

	@Column(name = "state")
	private String state;

	@Column(name = "pin_code") // Recommended to use a different name to avoid conflict with 'pin' keyword in some DBs
	private String pin;
}