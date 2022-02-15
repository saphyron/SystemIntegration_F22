package com.example.airlinerouter.configuration;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Constants {
    public static final String [] AIRLINES = {"SAS", "KLM", "Southwest"};
    public static ArrayList<String> AIRLINES_AL = null;
    public static final String INBOUND_QUEUE_NAME = "IN";
    public static final String DEPARTURE_QUEUE_NAME = "DEPARTURE";
    public static final String DEAD_LETTER_QUEUE_NAME = "dead.letter";
    public static final String FLIGHT_QUEUE_BASE = "flight.";
    public static final String DEPARTURE_QUEUE_BASE ="departure.";
    public static ArrayList<String> getAirlines(){
        if(AIRLINES_AL == null){
            AIRLINES_AL = new ArrayList<>();
            for(String al : AIRLINES){
                AIRLINES_AL.add(al);
            }
        }
        return AIRLINES_AL;
    }

}
