package com.temelius.drift86.web;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.temelius.drift86.model.*;
import com.temelius.drift86.util.FileUploadUtil;

@Controller
public class Drift86Controller {
	@Autowired
	private ScoreRepository srepository;
	
	@Autowired
	private CarRepository crepository;
	
	@Autowired
	private MapRepository mrepository;
	
	@Autowired
	private UserRepository urepository;
	
	// Show index page
	@RequestMapping(value = {"/", "/index"})
	public String index(Model model) {
		return "index";
	}
	
	/*
	 * Score related methods
	 */
	
	// Show all scores by map
	@RequestMapping(value = "/scores/{map}")
	public String scoresByMap(@PathVariable("map") long mapId, Model model) {
		Map map = mrepository.findById(mapId).get();
		model.addAttribute("scores", srepository.findAllByMapOrderByPointsDesc(map));
		return "mapscores";
	}
	
	// Show all maps
	@RequestMapping(value = "/scores")
	public String maps(Model model) {
		model.addAttribute("maps", mrepository.findAll());
		return "maps";
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
	public String save(Score score, @RequestParam("image") MultipartFile multipartFile) throws IOException {
		// Initiate FileUploadUtil
		FileUploadUtil uploadUtil = new FileUploadUtil();
		
		// Generate UUID filename with FileUploadUtil method
		String fileName = uploadUtil.generateUuidForImage(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
		
		// Set uuid image name to object
		score.setPhoto(fileName);
		
		srepository.save(score);
		
		// set upload path where the image should be uploaded
		// and finally save the image to the target directory
		String uploadDir = "images/";
		FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		
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
	@PreAuthorize("hasAuthority('ADMIN')")
	public String editScore(@PathVariable("id") Long scoreId, Model model) {
		model.addAttribute("score", srepository.findById(scoreId));
		model.addAttribute("cars", crepository.findAll());
		model.addAttribute("maps", mrepository.findAll());
		return "editscore";
	}
	
	@GetMapping(value="/verify/{id}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String verifyScore(@PathVariable("id") Long scoreId) {
		Score score = srepository.findById(scoreId).get();
		score.setVerified(true);
		srepository.save(score);
		return "redirect:../admin";
	}
	
	/*
	 * User related methods
	 */
	
	@RequestMapping(value = "/admin")
	public String adminPanel(Model model) {
		model.addAttribute("unverifiedScores", srepository.findAllByVerified(false));
		model.addAttribute("users", urepository.findAllByOrderByRoleAsc());
		return "adminpanel";
	}
	
	// Login page
	@RequestMapping(value = "/login")
	public String login() {
		return "login";
	}
	
	@RequestMapping(value = "/logout")
	public String logout() {
		return "logout";
	}
	
    @RequestMapping(value = "/register")
    public String addUser(Model model){
    	model.addAttribute("userdto", new UserDto());
        return "register";
    }
    
    /**
     * Create new user
     * Check if user already exists & form validation
     * 
     * @param signupForm
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "saveuser", method = RequestMethod.POST)
    public String saveUser(@Valid @ModelAttribute("userdto") UserDto userDto, BindingResult bindingResult) {
    	String emailRegex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    	if (!bindingResult.hasErrors()) { // validation errors
    		if (userDto.getPassword().equals(userDto.getPasswordCheck())) { // check password match		
	    		String pwd = userDto.getPassword();
		    	BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		    	String hashPwd = bc.encode(pwd);
		    	
		    	Pattern pattern = Pattern.compile(emailRegex);
		    	Matcher matcher = pattern.matcher(userDto.getEmail());
		    	if(!matcher.matches()) {
		    		bindingResult.rejectValue("email", "err.email", "Email doesn't meet requirements");
		    		return "register";
		    	}
	
		    	User newUser = new User();
		    	newUser.setPasswordHash(hashPwd);
		    	newUser.setUsername(userDto.getUsername());
		    	newUser.setEmail(userDto.getEmail());
		    	newUser.setRole("USER");
		    	if (urepository.findByUsername(userDto.getUsername()) == null) { // Check if user exists
		    		urepository.save(newUser);
		    	}
		    	else {
	    			bindingResult.rejectValue("username", "err.username", "Username already exists");    	
	    			return "register";		    		
		    	}
    		}
    		else {
    			bindingResult.rejectValue("passwordCheck", "err.passCheck", "Passwords does not match");    	
    			return "register";
    		}
    	}
    	else {
    		return "register";
    	}
    	return "redirect:/login";    	
    }    
    
}
