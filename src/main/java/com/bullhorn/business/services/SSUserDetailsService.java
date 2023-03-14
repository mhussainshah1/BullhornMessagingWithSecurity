package com.bullhorn.business.services;

import com.bullhorn.business.entities.User;
import com.bullhorn.business.entities.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class SSUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public SSUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            var appUser = userRepository.findByUsername(username);
            if (appUser == null) {
                System.out.println("User not found with the provided username" + appUser.toString());
                return null;
            }
            System.out.println("User from username " + appUser.getUsername());
            return new CustomerUserDetails(appUser, getAuthorities(appUser));
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }

    private Set<GrantedAuthority> getAuthorities(User appUser) {
        var authorities = new HashSet<GrantedAuthority>();
        for (var role : appUser.getRoles()) {
            var grantedAuthority = new SimpleGrantedAuthority("ROLE_" + role.getRole());
            authorities.add(grantedAuthority);
        }
        System.out.println("User authorities are" + authorities.toString());
        return authorities;
    }
}
