package com.example.covidImpact.Config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {
    @Bean("users")
    @Scope( value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public LoggedInUsers getLoggedInUsers() {
        return new LoggedInUsers();
    }
    
}
