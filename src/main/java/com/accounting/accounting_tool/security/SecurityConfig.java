package com.accounting.accounting_tool.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http.authorizeHttpRequests((authorize) -> {
            authorize
                // Routes without authentication
                .requestMatchers(HttpMethod.POST, "api/signup").permitAll()
                .requestMatchers(HttpMethod.POST, "api/login").permitAll()
                // .requestMatchers(HttpMethod.GET, "api/test").hasAnyRole("PERSONAL_ACCOUNTANT")
                .anyRequest().authenticated();
        });

        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

    /*
    * Search the data in the database
    * */
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource)
    {
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);

        userDetailsManager.setUsersByUsernameQuery(
            "select username, password, true from users where username = ?"
        );

        userDetailsManager.setAuthoritiesByUsernameQuery(
            "select users.username, roles.name from roles "
            + "inner join users on users.role_id = roles.role_id "
            + "where users.username = ?"
        );

        return userDetailsManager;
    }

    @Bean
    public static PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        UserDetailsManager userDetailsManager,
        PasswordEncoder passwordEncoder
    ) throws Exception
    {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setUserDetailsService(userDetailsManager);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public HttpSessionSecurityContextRepository httpSessionSecurityContextRepository()
    {
         return new HttpSessionSecurityContextRepository();
    }
}
