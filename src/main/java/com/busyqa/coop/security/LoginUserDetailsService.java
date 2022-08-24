package com.busyqa.coop.security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.busyqa.coop.jpa.User;
import com.busyqa.coop.repository.IUserRepository;

@Service	
public class LoginUserDetailsService implements UserDetailsService{
	
    @Autowired
	IUserRepository userRepository;
    
    
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = this.userRepository.findByUsername(username);
		
		if(user !=null) {
			return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), 
					true, true, true, true, getAuthorities(user.getRole()));
		}
		
		else {
			throw new UsernameNotFoundException("Username NOT found");
		}
	}
	
	private Collection<? extends GrantedAuthority> getAuthorities(String role){
		return Arrays.asList(new SimpleGrantedAuthority(role));
	}
}

