package com.learn.springsecurity;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
@Component
public class filtter extends OncePerRequestFilter {
    @Autowired
    private jwthelper jwthelper;
    @Autowired
    private UserDetailService UserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
       final String authheader =request.getHeader("Authrization");
       String username =null;
       String jwt = null;
       if(authheader != null && authheader.startsWith("Bearer ")){
        jwt = authheader.substring(7);
        username =jwthelper.extractUsername(jwt);
       }
        if(username!=null && SecurityContextHolder.getContext().getAuthentication() ==null){
            UserDetails userDetails = this.UserDetailsService.loadUserByUsername(username);
            if(this.jwthelper.validateToken(jwt, userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }            
        }
        filterChain.doFilter(request, response);
    }
    
}
