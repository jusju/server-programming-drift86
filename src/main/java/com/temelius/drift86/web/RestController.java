package com.temelius.drift86.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.temelius.drift86.model.Car;
import com.temelius.drift86.model.CarRepository;
import com.temelius.drift86.model.Map;
import com.temelius.drift86.model.MapRepository;
import com.temelius.drift86.model.Score;
import com.temelius.drift86.model.ScoreRepository;
import com.temelius.drift86.model.UserRepository;
import com.temelius.drift86.util.SearchService;

@Controller
@RequestMapping("api")
public class RestController {
	@Autowired
	private ScoreRepository srepository;

	@Autowired
	private CarRepository crepository;

	@Autowired
	private MapRepository mrepository;

	@Autowired
	private UserRepository urepository;
	
	@Autowired
	private SearchService sservice;
	
	/**
	 * 
	 * Score related methods
	 * 
	 */

	// RESTful - Get all scores by map
	@GetMapping(value = "scores/{map}")
	public @ResponseBody List<Score> scoresByMapRest(@PathVariable("map") long mapId) {
		Map map = mrepository.findById(mapId).get();
		return (List<Score>) srepository.findAllByMapOrderByPointsDesc(map);
	}

	// RESTful - get score by id
	@GetMapping(value = "score/{id}")
	public @ResponseBody Optional<Score> findScoreRest(@PathVariable("id") Long scoreId) {
		return srepository.findById(scoreId);
	}
	
	/**
	 * 
	 * Asset related methods
	 * 
	 */
	
	// Get all maps
	@GetMapping(value = "maps")
	public @ResponseBody List<Map> getAllMaps() {
		return (List<Map>) mrepository.findAll();
	}
	
	// Get all cars
	@GetMapping(value = "cars")
	public @ResponseBody List<Car> getAllCars() {
		return (List<Car>) crepository.findAll();
	}
	
	// Search car by name
	// TODO does not work yet.
	@GetMapping(value = "cars/{searchword}")
	public @ResponseBody List<Car> searchCar(String searchword) {
			return sservice.findByCarName(searchword);
		
	}
}
