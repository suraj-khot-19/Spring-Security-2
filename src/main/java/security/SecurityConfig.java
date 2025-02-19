package security;

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

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((authorize) -> ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)authorize.anyRequest()).authenticated()).formLogin(Customizer.withDefaults());

        return (SecurityFilterChain)http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){

        //password encoder
        PasswordEncoder encoder= PasswordEncoderFactories.createDelegatingPasswordEncoder();

        //user
        UserDetails user= User.withUsername("user").password(encoder.encode("password")).roles("USER").build();

        //admin
        UserDetails admin= User.withUsername("admin").password(encoder.encode("password")).roles("ADMIN").build();

        //jdbc based user details manager
        JdbcUserDetailsManager userDetailsManager=new JdbcUserDetailsManager();

        userDetailsManager.createUser(user);
        userDetailsManager.createUser(admin);

        return userDetailsManager;
    }
}
