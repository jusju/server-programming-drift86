package com.temelius.drift86.model;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CarRepository extends CrudRepository<Car, Long> {
	List<Car> findByName(String name);
	
	@Query(value = "SELECT * FROM Car c WHERE c.name ILIKE %:searchword%", nativeQuery=true)
    List<Car> findByCarName(@Param("searchword") String searchword);
}
