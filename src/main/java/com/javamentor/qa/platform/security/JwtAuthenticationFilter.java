package com.javamentor.qa.platform.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        try {
            String token = jwtUtil.resolveToken(request);
            if (token != null && jwtUtil.validateToken(token)) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(jwtUtil.extractUsername(token));

                if (!userDetails.isAccountNonLocked()) {
                    response.sendError(HttpStatus.FORBIDDEN.value(), "User is deleted");
                    return;
                }

                Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (ExpiredJwtException e) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Token expired");
            return;
        } catch (SignatureException e) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Wrong token signature");
            return;
        } catch (JwtException e) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            return;
        }

        chain.doFilter(request, response);

    }

}
