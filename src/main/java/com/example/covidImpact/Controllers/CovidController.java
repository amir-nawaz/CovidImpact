package com.example.covidImpact.Controllers;


import com.example.covidImpact.DTO.CovidReportCard;
import com.example.covidImpact.Utils.CSVUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/covid")
public class CovidController {
    
    @Autowired
    private Environment env;
    
    @Value( "${server.port}")
    private int serverPort;
    
    /*
    get the list of new cases.
    * */
    @GetMapping("/newcases")
    public String getStatus()
    {
        try{
            List< CovidReportCard > covidReportCards = CSVUtils.readCSVFileFromUrl();
    
        } catch (Exception e) {
            System.out.println(e);
        
        }
        
        return "Welcome to Covid Controller!!";
    }
    
    /*
    get list of new cased orderby number of no of casses.
    @param ascending : boolean
    * */
    
    @GetMapping("/newcases/sortedByCases")
    public  List<CovidReportCard> getCasesSortedByOrder(boolean ascending)
    {
        List< CovidReportCard > covidReportCards = new ArrayList<>();
        try{
            covidReportCards = CSVUtils.readCSVFileFromUrl();
            if(ascending)
            {
                covidReportCards.sort( Comparator.comparingInt( (CovidReportCard c) -> c.noOfcases ).reversed());
            }
            else{
                covidReportCards.sort( Comparator.comparingInt( (CovidReportCard c) -> c.noOfcases ));
            }
            
        } catch (Exception e) {
            System.out.println(e);
            
        }
        
        return covidReportCards;
    }
    
    /*
    list number of cases for provided country
    @param country : string -> name of country
    * */
    
    @GetMapping("/newcases/country/{country}")
    public  CovidReportCard getCasesForCountry(@PathVariable String country)
    {
        List< CovidReportCard > covidReportCards = new ArrayList<>();
        int totalCases = 0;
        
        try{
            covidReportCards = CSVUtils.readCSVFileFromUrl();
            totalCases =
                    covidReportCards.stream().filter( p -> p.country.equals( country ) ).mapToInt( x-> x.noOfcases ).sum();
            
        } catch (Exception e) {
            System.out.println(e);
            
        }
        
        return new CovidReportCard(country, totalCases);
    }
    
    /*
    get first N countries with highest number of cases
    @param noOfCountry : int -> value of N
    * */
    @GetMapping("/newcases/topcountries/{noOfCountry}")
    public List<CovidReportCard> getCasesForCountry(@PathVariable int noOfCountry)
    {
        List< CovidReportCard > covidReportCards = new ArrayList<>();
        
        try{
            covidReportCards =  getCasesSortedByOrder(true).stream().limit( noOfCountry ).collect(Collectors.toList());
        } catch (Exception e) {
            System.out.println(e);
            
        }
        
        return covidReportCards;
    }
}
