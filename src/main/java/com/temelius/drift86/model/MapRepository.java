package com.temelius.drift86.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface MapRepository extends CrudRepository<Map, Long> {
	List<Map> findByName(String name);

}
