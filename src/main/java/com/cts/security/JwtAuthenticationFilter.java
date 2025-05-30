package com.cts.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cts.exceptions.CustomerAppException;

import java.io.IOException;
@Component
public  class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;


    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {

            String token = getTokenFromRequest(request);
            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {

                String username = jwtTokenProvider.getName(token);
                // Load user details from the database
                System.out.println(username + "====================");
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                System.out.println(token);
                System.out.println(username);
                System.out.println(userDetails.getAuthorities());
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                System.out.println(userDetails.getAuthorities() + "=============");
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }






        }
        catch(CustomerAppException e) {
           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
           response.setContentType("application/json");
           response.getWriter().write("{\"error\":" + e.getMessage() + "}");
           return;
        }
        filterChain.doFilter(request, response);




    }

    private String getTokenFromRequest(HttpServletRequest request) {

        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
