using System;
using System.Collections.Generic;
using System.Text;
using System.Xml.Serialization;

namespace AirlineMessageSplitter.Messages
{
	[XmlRoot("FlightDetailsInfoResponse")]
	public class FlightDetailsInfoResponse
	{
		[XmlElement(ElementName = "Flight")]
		public Flight flight;
		[XmlElement(ElementName = "Passenger")]
		public Passenger passenger;
		[XmlElement(ElementName = "Luggage")]
		public List<Luggage> luggageList;
	}
}
