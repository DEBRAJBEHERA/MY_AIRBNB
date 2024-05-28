package com.airbnb.config;

import com.airbnb.entity.PropertyUser;
import com.airbnb.repository.PropertyUserRepository;
import com.airbnb.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {//it is a abstract class

    private JWTService jwtService;
    private PropertyUserRepository propertyUserRepository;

    public JWTRequestFilter(JWTService jwtService, PropertyUserRepository propertyUserRepository) {
        this.jwtService = jwtService;
        this.propertyUserRepository = propertyUserRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {//it is having http request comes to this method with jwt token
        String tokenHeader = request.getHeader("Authorization");//jwt token present in the header of Authorization part & request.getheader reading the token
        if (tokenHeader!=null && tokenHeader.startsWith("Bearer ")){//the token starts with "bearer "& token is present
            String token = tokenHeader.substring(8,tokenHeader.length()-1);//it remove the "bearer " and store the token in(" ")
            String username= jwtService.getUsername(token);//get the token then next is decrypt that token and goes to getUsername method
            Optional<PropertyUser> opUser = propertyUserRepository.findByUsername(username);//then to jwtservice username find in databse
            if(opUser.isPresent()){// if user details is present then it set that in a session
                PropertyUser user =opUser.get();
                //to keep tracking the current user login
                UsernamePasswordAuthenticationToken authentication =new UsernamePasswordAuthenticationToken(user,null, Collections.singleton(new SimpleGrantedAuthority(user.getUserRole())));
                authentication.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }
        filterChain.doFilter(request,response);
    }
}
