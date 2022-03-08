package com.example.airlinesplitter.beans;

import com.example.airlinesplitter.messages.Flight;
import com.example.airlinesplitter.messages.Luggage;
import com.example.airlinesplitter.messages.Passenger;
import org.springframework.context.annotation.Bean;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageSplitter {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    XPath xPath = XPathFactory.newInstance().newXPath();

    final String baseXPath = "/FlightDetailsInfoResponse";

    public MessageSplitter() throws ParserConfigurationException {
    }

    private Flight parseFlight(Document xmlDoc) throws XPathExpressionException {
        Node node = (Node) this.xPath.compile(baseXPath+"/Flight").evaluate(xmlDoc, XPathConstants.NODE);
        Flight flight = new Flight();
        flight.setId(UUID.randomUUID());
        flight.setFlightNumber(node.getAttributes().getNamedItem("number").getNodeValue());
        flight.setFlightDate(node.getAttributes().getNamedItem("Flightdate").getNodeValue());
        return flight;
    }

    private Passenger parsePassenger(Document xmlDoc, Flight flight) throws XPathExpressionException {
        Node passengerNode = (Node) xPath.compile(baseXPath+"/Passenger").evaluate(xmlDoc, XPathConstants.NODE);
        Passenger passenger = new Passenger();
        passenger.setId(UUID.randomUUID());
        passenger.setFirstName(xPath.compile("FirstName").evaluate(passengerNode));
        passenger.setLastName(xPath.compile("LastName").evaluate(passengerNode));
        passenger.setFlightId(flight.getId());
        passenger.setReservationNumber(xPath.compile("ReservationNumber").evaluate(passengerNode));
        return passenger;
    }

    private List<Luggage> parseLuggage(Document xmlDoc, Passenger passenger, Flight flight) throws XPathExpressionException {
        NodeList LuggageNodes = (NodeList) xPath.compile(baseXPath+"/Luggage").evaluate(xmlDoc, XPathConstants.NODESET);
        List<Luggage> ret = new ArrayList<>();
        for (int i = 0; i < LuggageNodes.getLength(); i++) {
            Node luggageNode = LuggageNodes.item(i);
            Luggage luggage = new Luggage();
            luggage.setFlightId(flight.getId());
            luggage.setPassengerId(passenger.getId());
            luggage.setId(xPath.compile("Id").evaluate(luggageNode));
            luggage.setCategory(xPath.compile("Category").evaluate(luggageNode));
            luggage.setWeight(Double.parseDouble(xPath.compile("Weight").evaluate(luggageNode)));
            luggage.setIdentification(xPath.compile("Identification").evaluate(luggageNode));
            ret.add(luggage);
        }
        return ret;
    }

    @Bean
    public List<Object> splitMessage(String xml) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {

        ArrayList<Object> ret = new ArrayList<>();
        Document xmlDoc = this.builder.parse(new InputSource(new StringReader(xml)));

        Flight flight = parseFlight(xmlDoc);
        ret.add(flight);

        Passenger passenger = parsePassenger(xmlDoc, flight);
        ret.add(passenger);

        List<Luggage> luggages = parseLuggage(xmlDoc, passenger, flight);
        ret.addAll(luggages);

        return ret;
    }
}
