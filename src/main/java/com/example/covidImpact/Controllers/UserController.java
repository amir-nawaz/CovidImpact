package com.example.covidImpact.Controllers;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private Environment env;
    
    @Value( "${server.port}")
    private int serverPort;
    
    @GetMapping("/status/check")
    public String getStatus()
    {
        return "working on port number: " + env.getProperty("local.server.port");
    }
    
    @GetMapping("/getJwt")
    public String getJwtToken()
    {
        String jwt = Jwts.builder()
                         .setSubject("Subject")
                         .signWith( SignatureAlgorithm.HS512, env.getProperty( "token.secret" ))
                         .compact();
        return jwt;
    }
}
