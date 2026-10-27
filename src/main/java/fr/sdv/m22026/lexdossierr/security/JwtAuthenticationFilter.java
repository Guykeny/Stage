package fr.sdv.m22026.lexdossierr.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain chain)throws ServletException, IOException{

        String bearerToken = request.getHeader("Authorization");

        if( bearerToken == null ||  !bearerToken.startsWith("Bearer ")){
            chain.doFilter(request, response);
            return;
        }
        String token= bearerToken.substring(7);

        try{
            Claims claims= jwtService.parseToken(token);
            String email= claims.getSubject();
            String role= claims.get("role", String.class);

            var authority= new SimpleGrantedAuthority("ROLE_"+role);
            var authentification= new UsernamePasswordAuthenticationToken(email,null, List.of(authority));
            SecurityContextHolder.getContext().setAuthentication(authentification);
        }catch (Exception e){
            System.out.println("Token refusé : " + e.getMessage());
        }

        chain.doFilter(request, response);
    }
}
