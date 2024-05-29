package ir.samin.appbuilder.security;

import ir.samin.appbuilder.dao.AdminRepository;
import ir.samin.appbuilder.dao.UserRepository;
import ir.samin.appbuilder.entity.Admin;
import ir.samin.appbuilder.entity.User;
import ir.samin.appbuilder.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserRepository userRepository;

    private AdminRepository adminRepository;

    private static final Logger logger = Logger.getLogger(SecurityConfig.class);

    private final UserService userService;
    private final JwtRequestFilter jwtRequestFilter;


    @Lazy
    public SecurityConfig(UserService userService,
                          JwtRequestFilter jwtRequestFilter,
                          AdminRepository adminRepository,
                          UserRepository userRepository) {
        this.userService = userService;
        this.jwtRequestFilter = jwtRequestFilter;
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // Set unauthorized requests exception handler
        http = http
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    ex.getMessage()
                            );
                        }
                )
                .and();
        // Set permissions on endpoints
        http.authorizeRequests()
                // Our public endpoints
                .antMatchers("/api/user/register","/api/user/login", "/api/user/sendSMS", "/api/user/confirm-code").permitAll()
                // Our private endpoints
                .anyRequest().authenticated();
//                .and()
//                .logout()
//                .logoutUrl("/api/user/logout") // Define logout URL
//                .logoutSuccessHandler(logoutSuccessHandler()) // Custom logout success handler
//                .invalidateHttpSession(true) // Invalidate session
//                .deleteCookies("JSESSIONID"); // Delete cookies if any;

        // Add JWT token filter
        http.addFilterBefore(
                jwtRequestFilter,
                UsernamePasswordAuthenticationFilter.class
        );
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {
        return username -> {
            Optional<User> user = userRepo.findByUsername(username);
            if (user.isPresent()) return new CustomUserDetails(user.get());

            throw new UsernameNotFoundException("User '" + username + "' not found");
        };
    }

    @Bean
    public UserDetailsService adminDetailsService(AdminRepository adminRepo) {
        return username -> {
            Optional<Admin> admin = adminRepo.findByUsername(username);
            if (admin.isPresent()) return new CustomAdminDetails(admin.get());

            throw new UsernameNotFoundException("Admin '" + username + "' not found");
        };
    }

    // Used by Spring Security if CORS is enabled.
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
