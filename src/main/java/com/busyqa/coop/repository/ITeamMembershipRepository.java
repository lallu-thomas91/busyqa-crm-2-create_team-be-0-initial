package com.busyqa.coop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.busyqa.coop.jpa.TeamMembership;

@Repository
public interface ITeamMembershipRepository extends JpaRepository<TeamMembership, Integer> {

}
