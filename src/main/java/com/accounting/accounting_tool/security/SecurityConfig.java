package com.accounting.accounting_tool.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig
{
    @Value("${cors.origin}")
    private String origin;
    private final RsaKeyProperties rsaKeys;

    @Autowired
    public SecurityConfig(RsaKeyProperties rsaKeys)
    {
        this.rsaKeys = rsaKeys;
    }

    @Bean
    UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(origin));
        configuration.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    /*
    * Configure the app as a Resource Server
    * */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        return http
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(Customizer.withDefaults())
            .authorizeHttpRequests((authorize) -> {
                authorize
                    // Routes without authentication
                    .requestMatchers(HttpMethod.GET, "/api/ok_prueba").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/signup").permitAll()

                    // ADMIN's Authorizations
                    .requestMatchers(HttpMethod.GET, "/api/users/search-username-coincidence/{name}").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/users/update-user/{id}").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/users/change-password-admin/{userId}").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/users/").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/users/delete/{id}").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.GET, "/api/roles/").hasRole("ADMIN")
                    .anyRequest().authenticated();
            })
            .oauth2ResourceServer((oauth2) -> oauth2
                    .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))) // We will use the JWT to the configuration
            // The session won't be stored or managed by HTTPSession
            // and, it won't be used it to get the security context.
            .build();
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
    * This allows us to decipher the JWT.
    * */
    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
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
    JwtEncoder jwtEncoder()
    {
        /*
        * We will encode the signature into a token
        * and sign it with the private key.
        * */
        JWK jwk = new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));

        return new NimbusJwtEncoder(jwks);
    }

    /*
    * This Bean defines how the Scope (the user's role) is extracted
    * to authorize the user.
    * */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter()
    {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();

        converter.setAuthoritiesClaimName("scope");
        converter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(converter);

        return jwtAuthenticationConverter;
    }
}
