package com.busyqa.coop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.busyqa.coop.jpa.Team;

@Repository
public interface ITeamRepository extends JpaRepository<Team, Integer> {
	
	Team findByIdTeam(int idTeam);
	Team findByTeamname(String teamname);

}
