package com.busyqa.coop.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.busyqa.coop.jpa.Team;
import com.busyqa.coop.jpa.TeamMembership;
import com.busyqa.coop.jpa.User;
import com.busyqa.coop.repository.ITeamRepository;
import com.busyqa.coop.repository.IUserRepository;
import com.busyqa.coop.repository.ITeamMembershipRepository;


@Service
public class TeamService {
	
	@Autowired
	ITeamRepository teamRepository;
	
	@Autowired
	IUserRepository userReposirory;
	
	@Autowired
	ITeamMembershipRepository teamMembershipReposirory;
	
	@Autowired
    UserService userService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Transactional(readOnly=true)
	public List<Team> listTeams(){
		List<Team> teams = this.teamRepository.findAll();
					
		return teams;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void createTeam(Team team, User user) {
		
		this.validateTeam(team);
				
		team.setCreatedby(user.getUsername());
		team.setCreatedOn(LocalDate.now());
		team.setMemberUsernames(new ArrayList<>());
		team.setTeamMemberships(new ArrayList<>());
		
		logger.debug("Creating new team " + team.toString());
		
		this.teamRepository.save(team);
		
		
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateTeam(Team newTeam, int idTeam) {
		
		this.validateTeam(newTeam);
		
		Team team = this.teamRepository.findByIdTeam(idTeam);
		if(team != null) {
			logger.debug("Updating team " + team.getTeamname() + "- Current member usernames of the Team: " + team.getMemberUsernames());
			
			team.setTeamname(newTeam.getTeamname());
			team.setTeamdescr(newTeam.getTeamdescr());
			
												
			//clear the old list of member users.
			this.userService.removeTeamMemberships(team.getTeamMemberships());
			this.teamMembershipReposirory.deleteInBatch(team.getTeamMemberships());
			this.teamMembershipReposirory.flush();
			team.getTeamMemberships().clear();
			
			List<String> newMemberUsernames = newTeam.getUsernames();
			/*
			 * Removing duplicate entries in membernames if any, using Set collection.
			 */
			Set<String> noDuplicate = new HashSet<>(newMemberUsernames);
			newMemberUsernames.clear();
			newMemberUsernames.addAll(noDuplicate);
			
			for(String username : newMemberUsernames) {
				User user = this.userReposirory.findByUsername(username);
				
				this.validateNewTeamMember(user, idTeam);
				TeamMembership teamMembership = this.createTeamMembership(user, team);
				
				// add new list of member users.
				
				team.addTeamMembership(teamMembership);
				logger.debug("Adding new TeamMembership! New member usernames of the Team: " + team.getMemberUsernames());
			}
			
			this.teamRepository.save(team);
			logger.debug("Updated Team: " + team.toString());
		}
	}
	
	
	@Transactional(readOnly=true)
	public Team findTeambyId(int idTeam) {
		return this.teamRepository.findById(idTeam).get();
	}
	
	
	@Transactional(readOnly=true)
	public Team findTeambyName(String teamname) {
		return this.teamRepository.findByTeamname(teamname);
	}
	
	
	@Transactional(propagation=Propagation.REQUIRED)
	public void deleteTeam(int idTeam) {
		Team team = this.teamRepository.findByIdTeam(idTeam);
		String teamname = this.teamRepository.findByIdTeam(idTeam).getTeamname();
		/*
		 * Removing this teamname from each member user
		 */
		team.getTeamMembers().forEach(user -> {
			List<String> teamnames = new ArrayList<>();
			teamnames = user.getTeamnames();
			teamnames.remove(teamname);
			user.setTeamnames(teamnames);
			this.userReposirory.save(user);
		});
		
		this.teamRepository.deleteById(idTeam);
	}
	
	
	private TeamMembership createTeamMembership (User user, Team team) {
		TeamMembership teamMembership = new TeamMembership();
		
		teamMembership.setUser(user);
		teamMembership.setTeam(team);
		
		return teamMembership;
	}
	
	
	private void validateNewTeamMember(User user, int idTeam) {
		List<TeamMembership> teamMemberships = user.getTeamMemberships();
		teamMemberships.removeIf(t -> t.getTeam().getIdTeam()==idTeam);
		
		if(teamMemberships.size()>=2) {
			throw new RuntimeException("User can be member of maximum two teams only. " + user.getUsername() + " " + user.getTeamnames());
		}
	}
	
	
	private void validateTeam (Team team) {
		Team existTeam = this.teamRepository.findByTeamname(team.getTeamname());
		if ((existTeam != null) && (existTeam.getIdTeam() != team.getIdTeam())) {
			throw new RuntimeException ("Team " + team.getTeamname() + " already exists.");
		}
	}

}
