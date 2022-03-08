using System;
using System.Collections.Generic;
using System.Text;
using System.Xml.Serialization;

namespace AirlineMessageSplitter.Messages
{
	/*
	 <Flight number="SK937" Flightdate="20190225">
		<Origin>ARLANDA</Origin>
		<Destination>LONDON</Destination>
	 </Flight>
	*/
	public class Flight
	{
		[XmlIgnore]
		public Guid flightId;
		[XmlAttribute("number")]
		public string flightNo;
		[XmlAttribute("Flightdate")]
		public string flightDate;
		[XmlElement(ElementName = "Origin")]
		public string origin;
		[XmlElement(ElementName = "Destination")]
		public string destination;
	}
}
