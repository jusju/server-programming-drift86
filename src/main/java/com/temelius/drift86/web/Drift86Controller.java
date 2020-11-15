package com.temelius.drift86.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.temelius.drift86.model.*;

@Controller
public class Drift86Controller {
	@Autowired
	private ScoreRepository srepository;
	
	@Autowired
	private CarRepository crepository;
	
	@Autowired
	private MapRepository mrepository;
	
	// Show index page
	@RequestMapping(value = {"/", "/index"})
	public String index(Model model) {
		return "index";
	}

	// Show all scores by map
	@RequestMapping(value = "/scores/{map}")
	public String scoresByMap(@PathVariable("map") long mapId, Model model) {
		Map map = mrepository.findById(mapId).get();
		model.addAttribute("scores", srepository.findAllByMap(map));
		return "mapscores";
	}
	
	// Show all maps
	@RequestMapping(value = "/scores")
	public String maps(Model model) {
		model.addAttribute("maps", mrepository.findAll());
		return "maps";
	}
	
	// RESTful - Get all scores by map
	@RequestMapping(value = "/api/scores/{map}", method = RequestMethod.GET)
	public @ResponseBody List<Score> scoresByMapRest(@PathVariable("map") long mapId ) {
		Map map = mrepository.findById(mapId).get();
		return (List<Score>) srepository.findAllByMap(map);
	}
	
	// RESTful - get score by id
	@RequestMapping(value = "/api/score/{id}", method = RequestMethod.GET)
	public @ResponseBody Optional<Score> findScoreRest(@PathVariable("id") Long scoreId) {
		return srepository.findById(scoreId);
	}
	
	// Add new score
	@RequestMapping(value = "/add")
	public String addScore(Model model) {
		model.addAttribute("score", new Score());
		model.addAttribute("cars", crepository.findAll());
		model.addAttribute("maps", mrepository.findAll());
		return "addscore";
	}
	
	// Save new score
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Score score) {
		srepository.save(score);
		return "redirect:/";
	}
	
	// Delete specified score
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasAuthority('ADMIN')")
	public String deleteScore(@PathVariable("id") Long scoreId, Model model) {
		srepository.deleteById(scoreId);
		return "redirect:../index";
	}
	
	// Edit specified score
	@RequestMapping(value = "/edit/{id}")
	public String editScore(@PathVariable("id") Long scoreId, Model model) {
		model.addAttribute("score", srepository.findById(scoreId));
		model.addAttribute("cars", crepository.findAll());
		model.addAttribute("maps", mrepository.findAll());
		return "editscore";
	}
	
	// Login page
	@RequestMapping(value = "/login")
	public String login() {
		return "login";
	}
}
