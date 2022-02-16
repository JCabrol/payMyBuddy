package com.openclassrooms.payMyBuddy.configuration;

import com.openclassrooms.payMyBuddy.repository.PersonRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private PersonRepository personRepository;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/","/home","/inscription").permitAll()
                .antMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/", true)
                .permitAll()
                .and()
                .logout().logoutUrl("/logout").logoutUrl("/");

    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
