package com.temelius.drift86.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Car {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long carId;
	private String name;
	private String description;
	
	@JsonBackReference
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "car")
	private List<Score> scores;
	
	public Car() {}
	
	public Car(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public long getId() {
		return carId;
	}

	public void setId(long id) {
		this.carId = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}


