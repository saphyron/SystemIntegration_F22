package com.example.airlinesplitter.configuration;

import lombok.Data;

import java.util.List;

@Data
public class Constants {
    public static final String [] AIRLINES = {"SAS", "KLM", "Southwest"};
    public static List<String> AIRLINES_AL = java.util.Arrays.asList(AIRLINES);
    public static final String INBOUND_QUEUE_NAME = "IN";
    public static final String DEPARTURE_QUEUE_NAME = "DEPARTURE";
    public static final String DEAD_LETTER_QUEUE_NAME = "dead.letter";
    public static final String FLIGHT_QUEUE_BASE = "flight.";
    public static final String DEPARTURE_QUEUE_BASE ="departure.";
    public static final String ETA_REQUEST_QUEUE_NAME = "request";
    public static final String ETA_REPLY_QUEUE_NAME = "reply";
}
