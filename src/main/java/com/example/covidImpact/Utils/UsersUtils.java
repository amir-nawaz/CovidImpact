package com.example.covidImpact.Utils;

import com.example.covidImpact.Config.LoggedInUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UsersUtils {
    @Autowired
    ApplicationContext context;
    
    /*
    * This class contains the hepling methods to get / set loggedin user details.
    * */
    
    public boolean isUserLoggedIn(String userName)
    {
        LoggedInUsers loggedInUsers = context.getBean( "users", LoggedInUsers.class );
    
        List< String > users =
                loggedInUsers.users.stream().filter( p -> p.equals( userName )  ).collect( Collectors.toList() );
        
        return users.isEmpty() ? false : true;
    }
    
    public void addLoggedInUser (String userName)
    {
        if(!isUserLoggedIn( userName )) {
            LoggedInUsers loggedInUsers = context.getBean( "users", LoggedInUsers.class );
            loggedInUsers.users.add( userName );
        }
    }
    
    public List<String> getLoggedInUsers () {
        LoggedInUsers loggedInUsers = context.getBean( "users", LoggedInUsers.class);
        return loggedInUsers.users;
    }
}
