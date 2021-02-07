package com.example.demo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


public class JwtTokenFilter extends GenericFilterBean {

    private JwtTokenServices jwtTokenServices;

    JwtTokenFilter (JwtTokenServices jwtTokenServices){
        this.jwtTokenServices = jwtTokenServices;
    }

    //the method is called for every request that comes in
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {

        //token sent from front-end
        String token = jwtTokenServices.getTokenFromRequest((HttpServletRequest) request);


        if(token != null && jwtTokenServices.validateToken(token)){
            Authentication auth = jwtTokenServices.parseUserFromTokenInfo(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
