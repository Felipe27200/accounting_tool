package com.accounting.accounting_tool.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import javax.sql.DataSource;
import java.security.interfaces.RSAPublicKey;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    @Value("${key.location}")
    RSAPublicKey key;

    /*
    * Configure the app as a Resource Server
    * */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http
            .authorizeHttpRequests((authorize) -> {
                authorize
                    // Routes without authentication
                    .requestMatchers(HttpMethod.POST, "api/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "api/signup").permitAll()
                    .anyRequest().authenticated();
            })
            .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));

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

    /*
    * +---------+
    * | ENCODER |
    * +---------+
    *
    * This wire an RSAPublic directly, we use NimbusJwtDecoder.
    *   This is for the Public key.
    *
    * The encoder will be used to encode the signature.
    * */
    @Bean
    JwtDecoder jwtDecoder()
    {
        return NimbusJwtDecoder.withPublicKey(this.key).build();
    }
}
