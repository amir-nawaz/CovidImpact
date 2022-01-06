package com.example.covidImpact.Utils;

import com.example.covidImpact.DTO.CovidReportCard;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CSVUtils {
    public static List<CovidReportCard> readCSVFileFromUrl() throws Exception {
    
        List<CovidReportCard> covidReportCards = new ArrayList<>();
        // we can put this url in properties files as well.
        URL url = new URL( "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv");
    
        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase();
        // date format available in csv file.
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yy");
    
        Calendar calendar = Calendar.getInstance();
        // To get the yesterday's date.
        calendar.add( Calendar.DATE, - 1);
        System.out.println("Latest Date -> " + dateFormat.format( calendar.getTime() ));
        
        
        try( CSVParser csvParser = CSVParser.parse( url, StandardCharsets.UTF_8, csvFormat )) {
            for( CSVRecord csvRecord : csvParser) {
                CovidReportCard covidReportCard = new CovidReportCard(csvRecord.get("Country/Region"), Integer.valueOf( csvRecord.get(dateFormat.format( calendar.getTime()))));
                covidReportCards.add( covidReportCard );
                
                System.out.println(covidReportCard.toString());
            }
            } catch ( IOException e) {
            e.printStackTrace();
        }
        return covidReportCards;
    }
}
