using System;
using System.Collections.Generic;
using System.Text;
using System.Xml.Serialization;

namespace AirlineMessageSplitter.Messages
{
	/*
	<Passenger>
		<ReservationNumber>CA937200305251</ReservationNumber>
		<FirstName>Anders</FirstName>
		<LastName>And</LastName>
	</Passenger>
	*/
	public class Passenger
	{ 
		[XmlIgnore]
		public Guid passengerId;
		[XmlElement(ElementName = "ReservationNumber")]
		public String reservationNumber;
		[XmlElement(ElementName = "FirstName")]
		public string firstName;
		[XmlElement(ElementName = "LastName")]
		public string lastName;
	}
}
