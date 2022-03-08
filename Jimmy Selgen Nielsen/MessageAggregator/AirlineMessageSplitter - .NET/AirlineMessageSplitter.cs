using System;
using System.Collections.Generic;

namespace AirlineMessageSplitter
{
	class AirlineMessageSplitter
	{
		XmlParser xmlParser = new();
        readonly MessageSplitter msgSplitter = new();
		readonly MessageAggregator msgAggreagator = new();

		public List<Object> ReadInputFile(string filename)
		{
			string input = System.IO.File.ReadAllText(filename);
			return xmlParser.ParseXml(input);
		}

		static void Main(string[] args)
		{
			string queue = "REQUEST.QUEUE";
			AirlineMessageSplitter splitter = new AirlineMessageSplitter();
			List<Object> messages = splitter.ReadInputFile("/tmp/FlightDetailsInfoResponse.xml");
			splitter.msgSplitter.SendMessages(queue,messages);
			var ret = splitter.msgAggreagator.Receive(queue);
			Console.WriteLine(splitter.xmlParser.ObjectsToXml(ret));
		}
	}
}
