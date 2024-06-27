package com.aupair.aupaircl.security.jwt;

import com.aupair.aupaircl.security.service.JwtService;
import com.aupair.aupaircl.security.service.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private UserDetailsServiceImpl userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = jwtService.resolveToken(request);
            if (token == null) {
                filterChain.doFilter(request, response);
            return;
            }
            Claims claims = jwtService.resolveClaims(request);
            if (claims != null && jwtService.validateClaims(claims,token)){
                String username = claims.getSubject();
                UserDetails user = userDetailsService.loadUserByUsername(username);
                Authentication auth =
                        new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(auth);
                filterChain.doFilter(request, response);
            }else{
                log.error("Token expirado");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Token expirado");
            }
        }catch (Exception e){
            log.error("Error al filtrar: "+e.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN,e.getMessage());
        }
    }
}
