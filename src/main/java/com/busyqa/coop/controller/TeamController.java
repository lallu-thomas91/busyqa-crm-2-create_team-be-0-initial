package com.busyqa.coop.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.busyqa.coop.jpa.Team;
import com.busyqa.coop.jpa.User;
import com.busyqa.coop.service.TeamService;
import com.busyqa.coop.service.UserService;

@SuppressWarnings("unused")
@RestController
public class TeamController {
	
	@Autowired
	TeamService teamService;
	
	@Autowired
	UserService userService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@PostMapping("/teams")
    public void createTeam(@RequestBody Team team) throws Exception {
    	
    	String inputTeamname = team.getTeamname();
    	String creator = team.getCreatedby();
    	
    	User user = this.userService.findUser(creator);
    	
    	if (!(user.getRole().equalsIgnoreCase("Admin") || user.getRole().equalsIgnoreCase("Manager"))){					//Checking if user is Admin or Manager
    		throw new Exception("User " + creator + " don't have permission to create team. Role: " + user.getRole());
    	}
    	
    	else {
    	logger.debug("Create new Team request: " + inputTeamname);

        this.teamService.createTeam(team, user);
    	}
    }
	
	@GetMapping("/teams")
	public List<Team> teamList(){
				
		List<Team> teams = this.teamService.listTeams();

		logger.debug("List Teams {} :");
		teams.forEach(System.out::println);
		
		return teams;
	}
	
	 @GetMapping("/team/{idTeam}")
	 public Team listTeam(@PathVariable("idTeam") int idTeam){
		 
		 Team team = this.teamService.findTeambyId(idTeam);
		 logger.debug("Fetch team details to update " + team );
		 return team;
	 }
	 
	 @PutMapping("/team/update/{idTeam}")
	 public void updateTeam(@RequestBody Team updatedTeam, @PathVariable("idTeam") int idTeam) {
		 
		 logger.debug("Update Team : "+ idTeam);
		 this.teamService.updateTeam(updatedTeam, idTeam);
		 
	 }
	 
	 @DeleteMapping("/team/{idTeam}")
	    public void deleteUser(@PathVariable("idTeam") int idTeam){

	        logger.debug("Delete Team!" + idTeam);

	        this.teamService.deleteTeam(idTeam);
	    }

	 /*
	 @PutMapping("/teams/{teamname}/add/{username}")
	 public void addTeamMember(@PathVariable("teamname") String teamname, @PathVariable("username") String username) {
		 
		 logger.debug("Adding user: "+ username + " to the team: " + teamname);
		 
		 User user = this.userService.findUser(username);
		 
		 this.teamService.addTeamMember(user, teamname);
	 }
	 */
	 
	 /*
	  @PutMapping("/teams/{teamname}/add")
	 public void addTeamMember(@PathVariable("teamname") String teamname, @RequestBody List<String> usernames) {
		 	 
		 List<User> users = new ArrayList<>();
		 
		 usernames.forEach(username -> users.add(this.userService.findUser(username)));
		 
		 users.forEach(user -> {
			 logger.debug("Adding user: "+ user.getUsername() + " to the team: " + teamname);
			 this.teamService.addTeamMember(user, teamname);
		 });
	 }
	 */
	 
	 /*
	 @PutMapping("/teams/{teamname}/remove")
	 public void removeTeamMember(@PathVariable("teamname") String teamname,  @RequestBody String username) {
		 
		 logger.debug("Removing user: "+ username + " from the team: " + teamname);
		 
		 User user = this.userService.findUser(username);
		 
		 this.teamService.removeTeamMember(user, teamname);
	 }
	 */
}
