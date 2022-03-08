using System;
using System.Collections.Generic;
using System.Text;
using System.Xml.Serialization;

namespace AirlineMessageSplitter.Messages
{
	/*
	   <Luggage>
		<Id>CA937200305252</Id>
		<Identification>2</Identification>
		<Category>Large</Category>
		<Weight>22.7</Weight>
	   </Luggage>
	 */

	public class Luggage
	{
		
		[XmlElement(ElementName = "Id")]
		public string id;
		[XmlElement(ElementName = "Identification")]
		public string identification;
		[XmlElement(ElementName = "Category")]
		public string category;
		[XmlElement(ElementName = "Weight")]
		public double weight;
		[XmlIgnore]
		public Guid passengerId;
		[XmlIgnore]
		public Guid flightId;
	}
}
