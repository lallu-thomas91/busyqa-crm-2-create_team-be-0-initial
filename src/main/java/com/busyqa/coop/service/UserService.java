package com.busyqa.coop.service;


import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.busyqa.coop.jpa.Team;
import com.busyqa.coop.jpa.TeamMembership;
import com.busyqa.coop.jpa.User;
import com.busyqa.coop.repository.ITeamMembershipRepository;
import com.busyqa.coop.repository.ITeamRepository;
import com.busyqa.coop.repository.IUserRepository;
//import com.busyqa.coop.security.AuthenticationFilter;

@Service
public class UserService {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired 
	PasswordEncoder passwordEncoder;
	
    @Autowired
	IUserRepository userRepository;

	@Autowired
	ITeamRepository teamRepository;

	@Autowired
	ITeamMembershipRepository teamMembershipReposirory;

    
    @Transactional(propagation=Propagation.REQUIRED)
    public void signupUser(User user){
    	
    	this.validateNewUser(user);
    	    	
    	user.setPassword(passwordEncoder.encode(user.getPassword()));
    	
//    	Initializing teamnames and teamMemberships for user with empty array before saving.
    	user.setTeamnames(new ArrayList<>());
    	user.setTeamMemberships(new ArrayList<>());
    	
    	logger.debug("Signup new user attempt " + user.toString());
    	
    	this.userRepository.save(user);
    }
    
    @Transactional(readOnly=true)
    public List<User> listValidUsers(int idTeam) {
    	/*
    	 * Fetching list of users available for the requested team.
    	 * i.e, remove users already member of this team and
    	 * remove users member of 2 teams already which is max allowed for a user.
    	 */
        List<User> validUsers = this.userRepository.findAll();
        String teamname = this.teamRepository.findByIdTeam(idTeam).getTeamname();
        
        List<User> invalidUsers = new ArrayList<>();  /*Defining separate list for invalid users which will be removed from valid users list.
         												To avoid concurrentModificationException which occurs if try to modify a list with same list in advanced loop*/
        
        validUsers.forEach(user -> {
        	/*
        	 * adding user already member of this team to invalid list
        	 */     	
        	user.getTeamnames().forEach(name -> {
        		if(name.equals(teamname)) {
					invalidUsers.add(user);	
				}
        	});
        	
        	/*
        	 * adding user already member of 2 teams to invalid list
        	 */
        	if(user.getTeamMemberships().size() >= 2) {
    			invalidUsers.add(user);
    		}
        });
        
        validUsers.removeAll(invalidUsers);		/*Removing invalid users from valid users list. */

        return validUsers;
    }
    
      
    @Transactional(readOnly=true)
    public User authenticateUser(String inputUsername, String inputPassword) {
    	
    	String encodedPassword = passwordEncoder.encode(inputPassword);
    	
    	User user = this.userRepository.findByUsernameAndPassword(inputUsername, encodedPassword);
       
    	logger.debug("Available user with username " + inputUsername + ": " + user);
           
       if(user != null ){
    	   
    	   return user;
       }
       else {
    	   throw new BadCredentialsException("Invalid credentials");
       }
	}
    
    
    @Transactional(readOnly=true)
    public User findUser(String username){
       
    	return this.userRepository.findByUsername(username);
	}
    
    
    @Transactional(propagation=Propagation.REQUIRED)
    public void removeTeamMemberships(List<TeamMembership> teamMemberships) {
    	if(teamMemberships.size() >=1 ) {
    		List<User> users = new ArrayList<>();
        	Team team = teamMemberships.get(0).getTeam();
        	teamMemberships.forEach(teamMembership -> users.add(teamMembership.getUser()));
        	users.forEach(user -> {
        		user.getTeamMemberships().removeIf(t -> t.getTeam().equals(team));
        		this.userRepository.save(user);
        	});
    	}
    	
    }


    private void validateNewUser(User user) {
        /*
         * Validate user Data.
         */
        if (user.getFirstName().length()<6 ||
            user.getLastName().length()<6 ||
            user.getUsername().length()<6 ||
            user.getPassword().length()<6 ||
            user.getEmail().length()<6 ||
            user.getPhone().length()<10 ||
            user.getRole().isEmpty() ){
            	throw new RuntimeException("Invalid User Data " + user);
        	}
    }
}
