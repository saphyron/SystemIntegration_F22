using AirlineMessageSplitter.Messages;
using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Xml;

namespace AirlineMessageSplitter
{
	class XmlParser
	{

        public List<Object> ParseXml(string body)
        {
            List<Object> ret = new List<object>();
            XmlDocument xml = new XmlDocument();
			StringReader reader = new StringReader(body);
			string XMLDocument = reader.ReadToEnd().ToString();
			xml.LoadXml(XMLDocument);
            Flight flight = null;
            Passenger passenger = null;

            XmlNode itemNode = xml.SelectSingleNode("/FlightDetailsInfoResponse/Flight");
            if (itemNode != null)
            {
                flight = new Flight();
                flight.destination = itemNode.SelectSingleNode("Destination").InnerText;
                flight.origin = itemNode.SelectSingleNode("Origin").InnerText;
                flight.flightDate = itemNode.Attributes["Flightdate"].Value;
                flight.flightNo = itemNode.Attributes["number"].Value;
                flight.flightId = Guid.NewGuid();
                ret.Add(flight);
            }

            itemNode = xml.SelectSingleNode("/FlightDetailsInfoResponse/Passenger");
            if(itemNode != null)
			{
                passenger = new Passenger();
                passenger.reservationNumber = itemNode.SelectSingleNode("ReservationNumber").InnerText;
                passenger.firstName = itemNode.SelectSingleNode("FirstName").InnerText;
                passenger.lastName = itemNode.SelectSingleNode("LastName").InnerText;
                passenger.passengerId = Guid.NewGuid();
                ret.Add(passenger);
			}

            XmlNodeList luggageList = xml.SelectNodes("/FlightDetailsInfoResponse/Luggage");
            foreach (XmlNode luggageNode in luggageList)
			{
                Luggage luggage = new Luggage();
                luggage.id = luggageNode.SelectSingleNode("Id").InnerText;
                luggage.identification = luggageNode.SelectSingleNode("Identification").InnerText;
                luggage.category = luggageNode.SelectSingleNode("Category").InnerText;
                luggage.weight = Double.Parse(luggageNode.SelectSingleNode("Weight").InnerText);
                luggage.passengerId = passenger.passengerId;
                luggage.flightId = flight.flightId;
                ret.Add(luggage);
			}
            return ret;
        }

        public string ObjectsToXml(FlightDetailsInfoResponse message)
		{
            try
            {
                System.Xml.Serialization.XmlSerializer xmlSerializer = new System.Xml.Serialization.XmlSerializer(typeof(FlightDetailsInfoResponse));
                StringWriter writer = new StringWriter();
                xmlSerializer.Serialize(writer, message);
                return writer.ToString();
            }catch(Exception e)
			{
                Console.WriteLine(e.Message);
                throw;
			}
            
		}
	}
}
