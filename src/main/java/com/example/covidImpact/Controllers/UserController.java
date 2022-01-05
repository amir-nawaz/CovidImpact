package com.example.covidImpact.Controllers;


import com.example.covidImpact.Security.JwtRequest;
import com.example.covidImpact.Security.JwtResponse;
import com.example.covidImpact.Security.JwtTokenUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private Environment env;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;
    
    
    @Value( "${server.port}")
    private int serverPort;
    
    @GetMapping("/status/check")
    public String getStatus()
    {
        return "working on port number: " + env.getProperty("local.server.port");
    }
    
   /* @GetMapping("/getJwt")
    public String getJwtToken()
    {
        String jwt = Jwts.builder()
                         .setSubject("Subject")
                         .signWith( SignatureAlgorithm.HS512, env.getProperty( "token.secret" ))
                         .compact();
        return jwt;
    }*/
    
    @RequestMapping(value = "/getJwt", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
            throws Exception {
        
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        
        final UserDetails userDetails = jwtInMemoryUserDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        
        final String token = jwtTokenUtil.generateToken(userDetails);
        
        return ResponseEntity.ok(new JwtResponse( token) );
    }
    
    private void authenticate(String username, String password) throws Exception {
        Objects.requireNonNull( username );
        Objects.requireNonNull(password);
        
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken( username, password) );
        } catch ( DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch ( BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
