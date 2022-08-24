package com.busyqa.coop.controller;

import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.busyqa.coop.jpa.User;
import com.busyqa.coop.service.UserService;



@SuppressWarnings("unused")
@RestController
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserService userService;
    
      
    @RequestMapping("/login")
    public Principal user (Principal user) throws BadCredentialsException {
    	System.out.println("Principal user is " + user);
    	if (user != null){
    	return user;
    	}
    	else {
    		throw new BadCredentialsException("Invalid Credentials");
    	}
    }
    
    @PostMapping("/signup")
    public void signupUser(@RequestBody User user) throws Exception {
    	
    	String inputUsername = user.getUsername();
    	
    	User existUser = this.userService.findUser(inputUsername);
    	
    	if (existUser != null){
    		throw new Exception("Username " + inputUsername + " already exists.");
    	}
    	else {
    	logger.debug("New User signup!");

        this.userService.signupUser(user);
    	}
    }
    
    @GetMapping("/users/{idTeam}")
    public List<User> listValidUsers(@PathVariable("idTeam") int idTeam){
    	logger.debug("List valid Users available to add to team!" + idTeam);

        List<User> validUsers = this.userService.listValidUsers(idTeam);

        validUsers.forEach(System.out::println);

        return validUsers;
    }
    
    /*
     * Check if the username already exists in database while it's typed into signup form.
     * (allowed without authentication in security configuaration)
     */
    @GetMapping(value = "/check/{username}")
    public User checkUsernameExists(@PathVariable("username") String username) {
    	
    	return this.userService.findUser(username);
    }
    
    /*
     * Return logged in user details to get First and Last name 
     * to be displayed on Welcome screen.
     */
    @GetMapping(value = "/welcome/{username}")
    public User welcomeUser(@PathVariable("username") String username) {
    	
    	return this.userService.findUser(username);
    }
  
}
