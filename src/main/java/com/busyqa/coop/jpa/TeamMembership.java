package com.busyqa.coop.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/*
 *Junction Table for Team & User 
 */

@Entity
@Table(name="crm_team_membership")
public class TeamMembership implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="ID_TEAM_MEMBERSHIP", unique=true, nullable=false)
    private int idTeamMembership;
	
	@ManyToOne
	@JsonIgnore
    @JoinColumn(name="ID_TEAM", nullable=false)
    private Team team;
	
	@ManyToOne
	@JsonIgnore
    @JoinColumn(name="USERNAME", nullable=false)
    private User user;

	public TeamMembership() {
	}

	public int getIdTeamMembership() {
		return idTeamMembership;
	}

	public void setIdTeamMembership(int idTeamMembership) {
		this.idTeamMembership = idTeamMembership;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
