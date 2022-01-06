package com.example.covidImpact.Controllers;


import com.example.covidImpact.Config.AppConfig;
import com.example.covidImpact.Config.LoggedInUsers;
import com.example.covidImpact.Security.JwtRequest;
import com.example.covidImpact.Security.JwtResponse;
import com.example.covidImpact.Security.JwtTokenUtil;
import com.example.covidImpact.Utils.UsersUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    
    @Autowired
    private ApplicationContext appContext;
    
    @Autowired
    UsersUtils usersUtils;
    
    
    @Value( "${server.port}")
    private int serverPort;
    
    /*
    test api to check status
    * */
    @GetMapping("/status/check")
    public ResponseEntity<String> getStatus()
    {
        return ResponseEntity.ok("working on port number: " + env.getProperty("local.server.port"));
    }
    
    /* get list of loggedIn Users
    *
    * */
    
    @GetMapping("/loggedin")
    public ResponseEntity<List<String>> getUsers()
    {
        return ResponseEntity.ok(usersUtils.getLoggedInUsers()) ;
    }
    
    /*
    * get Jwt for any provided user credentials.
    * */
    
    @RequestMapping(value = "/getJwt", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
            throws Exception {
        
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        
        final UserDetails userDetails = jwtInMemoryUserDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());
        
        final String token = jwtTokenUtil.generateToken(userDetails);
    
        // updateLoggedInUsers(authenticationRequest.getUsername());
        usersUtils.addLoggedInUser( authenticationRequest.getUsername() );
        
        return ResponseEntity.ok(new JwtResponse( token) );
    }
    
    private void updateLoggedInUsers(String userName) {
        
        LoggedInUsers loggedInUsers = appContext.getBean( "users", LoggedInUsers.class );
        
        List< String > users =
                loggedInUsers.users.stream().filter( p -> p.equals( userName )  ).collect( Collectors.toList() );
        if(users.isEmpty())
            loggedInUsers.users.add( userName );
        
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
