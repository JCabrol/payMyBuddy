package com.openclassrooms.payMyBuddy.service;

import com.openclassrooms.payMyBuddy.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private PersonService personService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Person person = personService.getPerson(email);
        GrantedAuthority authority = new SimpleGrantedAuthority(person.getRole().name());
        return new org.springframework.security.core.userdetails.User(
                person.getEmail(),
                person.getPassword(),
                Collections.singletonList(authority)
        );
    }
}
