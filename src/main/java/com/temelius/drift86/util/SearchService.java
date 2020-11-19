package com.temelius.drift86.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.temelius.drift86.model.Car;
import com.temelius.drift86.model.CarRepository;

@Service
public class SearchService {
	@Autowired
    private CarRepository crepository;
     
    public List<Car> findByCarName(String searchword) {
    	return crepository.findByCarName(searchword);
    }
}
