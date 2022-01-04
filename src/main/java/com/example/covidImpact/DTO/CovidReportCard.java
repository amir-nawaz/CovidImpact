package com.example.covidImpact.DTO;

public class CovidReportCard {
    public String country;
    public int noOfcases;
    
    public CovidReportCard() {
    
    }
    
    public CovidReportCard(String country, int noOfCases) {
        this.country = country;
        this.noOfcases = noOfCases;
    }
    
    @Override
    public String toString() {
        return this.country + " -> " + this.noOfcases;
    }
}
