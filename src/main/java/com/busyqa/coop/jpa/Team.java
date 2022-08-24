package com.busyqa.coop.jpa;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name="crmteams")
public class Team implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @Column(name="ID_TEAM", unique=true, nullable=false)
    private int idTeam;
	
    @Column(name="TEAM_NAME", unique=true, nullable=false, length=45)
    private String teamname;
	
	@Column(name="TEAM_DESCR", nullable=false, length=100)
    private String teamdescr;
	
	@Column(name="CREATED_BY", nullable=false)
	private String createdby;
	
	@Column(name="DATE_OF_CREATION", nullable=false)
    private LocalDate createdOn;
		
	@Transient
	private List<String> memberUsernames;
	
	@JsonIgnore
	@OneToMany(mappedBy="team", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private List<TeamMembership> teamMemberships;
	
	/*
	 * Constructors
	 */
	public Team() {
		
	}
	
	public Team(int idTeam, String teamname, String teamdescr, String createdby,
				LocalDate createdOn, List<String> memberUsernames, List<TeamMembership> teamMemberships) {
		this.idTeam = idTeam;
		this.teamname = teamname;
		this.teamdescr = teamdescr;
		this.createdby = createdby;
		this.createdOn = createdOn;
		this.memberUsernames = memberUsernames;
		this.teamMemberships = teamMemberships;
	}
	
	/*
	 * Getters & Setters
	 */
	public int getIdTeam() {
		return idTeam;
	}

	public void setIdTeam(int idTeam) {
		this.idTeam = idTeam;
	}

	public String getTeamname() {
		return teamname;
	}

	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}

	public String getTeamdescr() {
		return teamdescr;
	}

	public void setTeamdescr(String teamdescr) {
		this.teamdescr = teamdescr;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public LocalDate getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDate createdOn) {
		this.createdOn = createdOn;
	}

	public List<TeamMembership> getTeamMemberships() {
		return teamMemberships;
	}

	public void setTeamMemberships(List<TeamMembership> teamMemberships) {
		this.teamMemberships = teamMemberships;
	}
	

	public List<String> getMemberUsernames() {
		List<String> memberUsernames = new ArrayList<>();
		this.getTeamMembers().forEach(user -> memberUsernames.add(user.getUsername()));
		return memberUsernames;
	}
	
	@JsonIgnore
	public List<String> getUsernames() {
		return memberUsernames;
	}
	

	public void setMemberUsernames(List<String> memberUsernames) {
		this.memberUsernames = memberUsernames;
	}

	
	/*
	 * Custom methods for retrieving member user details
	 * add JSonIgnore to avoid recursion
	 */
	@JsonIgnore
	public List<User> getTeamMembers() {
		List<User> teamMembers = new ArrayList<>();
		this.teamMemberships.forEach(teamMembership -> teamMembers.add(teamMembership.getUser()));
		return teamMembers;
	}
		
	public TeamMembership addTeamMembership(TeamMembership teamMembership) {
		
//		if (teamMembership.getUser().getTeamMemberships().size() < 2) {		//Checking if the user is a member of 2 teams already
//			
//			List<TeamMembership> newteamMemberships = this.getTeamMemberships();
//			newteamMemberships.add(teamMembership);
//			this.setTeamMemberships(newteamMemberships);
//			
//			teamMembership.setTeam(this);	
//		}
		this.getTeamMemberships().add(teamMembership);
		teamMembership.setTeam(this);
		return teamMembership;
	}
	
	
	public TeamMembership removeTeamMembership(TeamMembership teamMembership) {
		
//		List<TeamMembership> newteamMemberships = getTeamMemberships();
//		newteamMemberships.remove(teamMembership);
//		
//		this.setTeamMemberships(newteamMemberships);
		this.getTeamMemberships().remove(teamMembership);
		teamMembership.setTeam(null);
		teamMembership.setUser(null);
		return teamMembership;
	}
	
	@Override
	public String toString() {
        return "Team: [idTeam: " + idTeam + ", teamname: " + teamname + ", createdby: " + createdby + 
        		", createdOn: " + createdOn.toString() + ", memberUsernames: " + this.getMemberUsernames() + "]";
        		
	}

}
