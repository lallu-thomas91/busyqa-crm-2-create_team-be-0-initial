package com.busyqa.coop.jpa;

import java.io.Serializable;
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
@Table(name="busyqacrmusers")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
 
    @Column(name="ID_USER", unique=true, nullable=false)
    private int idUser;

    @Column(name="FIRST_NAME", nullable=false, length=45)
    private String firstName;

    @Column(name="LAST_NAME", nullable=false, length=45)
    private String lastName;

    /*
     * Username will be used as the ID to find a user from database 
     * and hence should be unique
     */
    @Id
    @Column(name="USERNAME", unique=true, nullable=false, length=45)
    private String username;

    @Column(name="PASSWORD",nullable=false, length=100)
    private String password;

    @Column(name="EMAIL", unique=true, nullable=false, length=45)
    private String email;
    
    @Column(name="PHONE_NUMBER", nullable=false)
    private String phone;
    
    @Column(name="ROLE",nullable=false, length=45)
    private String role;
    
    @Transient
    private List<String> teamnames;
    
//    @JsonIgnore
    @OneToMany(mappedBy="user", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private List<TeamMembership> teamMemberships;


    public User() {
    }

	public User(int idUser, String firstName, String lastName,
               String username, String password, String email, String phone, String role) {
        this.idUser = idUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }


    public int getIdUser() {
		return idUser;
	}

	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

    public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	
	public List<TeamMembership> getTeamMemberships() {
		return teamMemberships;
	}

	public void setTeamMemberships(List<TeamMembership> teamMemberships) {
		this.teamMemberships = teamMemberships;
	}
	
	
	public List<String> getTeamnames() {
		List<String> teamnames = new ArrayList<>();
		this.getTeams().forEach(team -> teamnames.add(team.getTeamname()));
		return teamnames;
	}

	public void setTeamnames(List<String> teamnames) {
		this.teamnames = teamnames;
	}

	/*
	 * Custom methods for retrieving member team details.
	 * add JSonIgnore to avoid recursion
	 */
	@JsonIgnore
	public List<Team> getTeams(){
		List<Team> teams = new ArrayList<>();
		this.getTeamMemberships().forEach(teamMembership -> teams.add(teamMembership.getTeam()));
		return teams;
	}

	public TeamMembership addTeamMembership(TeamMembership teamMembership) {
//		if (getTeamMemberships().size()<2) {		//Checking if the user is a member of 2 teams already.
//			
//			List<String> teamnames = getTeamnames();
//			teamnames.add(teamMembership.getTeam().getTeamname());
//			setTeamnames(teamnames);
//			
//			getTeamMemberships().add(teamMembership);
//			teamMembership.setUser(this);
//			}
		getTeamMemberships().add(teamMembership);
		teamMembership.setUser(this);
		return teamMembership;
	}
	
	public TeamMembership removeTeamMembership(TeamMembership teamMembership) {
		getTeamMemberships().remove(teamMembership);
		teamMembership.setUser(null);
		return teamMembership;
	}

	@Override
    public String toString() {
        return "User [idUser=" + idUser + ", firstName=" + firstName + ", lastName=" + lastName + ", username=" + username + 
               ", email=" + email + ", phone=" + phone + ", role= " + role + ", teamnames=" + teamnames + "]";
    }
}
