package com.thegame.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thegame.dto.AuthenticationUserObject;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceAuthenticationFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authenticationHeader = request.getHeader("X-USER-AUTH");
        try {
            AuthenticationUserObject appUser = objectMapper.readValue(authenticationHeader, AuthenticationUserObject.class);
            setAuthentication(request, appUser);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        filterChain.doFilter(request,response);
    }

    private void setAuthentication(HttpServletRequest request, AuthenticationUserObject user) {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.role().name()));
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user, null, authorities);
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
