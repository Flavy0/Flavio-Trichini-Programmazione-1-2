package com.example.bus.config;

import com.example.bus.service.JpaUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JpaUserDetailsService userDetailsService;

    private static final String USER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.test-jwt-token-per-user";
    private static final String USER_EMAIL = "flavy@test.it";

    private static final String ADMIN_TOKEN = "eyJhbGciOiJIUzI1NiJ9.test-jwt-token-per-admin";
    private static final String ADMIN_EMAIL = "admin@bus.com"; // Assicurati che questa email abbia il ruolo ADMIN

    public JwtAuthenticationFilter(JpaUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String jwt = null;
        String userEmail = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        }

        if (jwt != null) {
            if (USER_TOKEN.equals(jwt)) {
                userEmail = USER_EMAIL;
            } else if (ADMIN_TOKEN.equals(jwt)) {
                userEmail = ADMIN_EMAIL;
            }
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            try {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (userDetails != null) {
                    System.out.println("--- DEBUG JWT Filter ---");
                    System.out.println("Token riconosciuto per: " + userEmail);
                    System.out.println("Autorità caricate: " + userDetails.getAuthorities());
                    System.out.println("------------------------");

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                System.err.println("Autenticazione JWT simulata fallita: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}