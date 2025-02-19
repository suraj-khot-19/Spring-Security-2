package com.sprint_security.Spring_Security_2.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    /// datasource for SQL database
    @Autowired
    DataSource dataSource;

    /// security filter chain
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl) authorize.anyRequest()).authenticated()).formLogin(Customizer.withDefaults());

        /// header based login
        http.httpBasic(Customizer.withDefaults());
        return (SecurityFilterChain) http.build();
    }

    /// user details service
    @Bean
    public UserDetailsService userDetailsService() {
        //password encoder
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        //user
        UserDetails user = User.withUsername("user").password(encoder.encode("password")).roles("USER").build();

        //admin
        UserDetails admin = User.withUsername("admin").password(encoder.encode("password")).roles("ADMIN").build();

        //jdbc based user details manager
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

        //check for user
        if (!userDetailsManager.userExists("user"))
            userDetailsManager.createUser(user);
        //check for admin
        if (!userDetailsManager.userExists("admin"))
            userDetailsManager.createUser(admin);

        return userDetailsManager;
    }
}
