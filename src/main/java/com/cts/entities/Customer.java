package com.cts.entities;

import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.JoinColumn;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String firstName;
	private String lastName;
	private String startLocation;
	private String destination;
	@ManyToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
	@JoinTable(name = "customer_location", joinColumns = @JoinColumn(name = "customer_id"), inverseJoinColumns = @JoinColumn(name = "location_id"))
	private List<Location> locations;
	private String packageName;
	private Number cost;
	@ManyToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@JoinColumn(name = "permanent_address ")
	private Address permanentAddress;
	@ManyToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER)
	@JoinColumn(name = "communication_address")
	private Address communicationAddress;
	@Column(unique = true)
	private String phone;
	private String notes;
}
