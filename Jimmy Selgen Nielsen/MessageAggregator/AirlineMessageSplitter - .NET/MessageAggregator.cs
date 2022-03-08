using AirlineMessageSplitter.Messages;
using Apache.NMS;
using Apache.NMS.ActiveMQ;
using Apache.NMS.Util;
using System;
using System.Collections.Generic;
using System.Text;

namespace AirlineMessageSplitter
{
	class MessageAggregator
	{
		public ITextMessage ReceiveMessage(string queue, ISession session, string selector=null)
		{
			ITextMessage ret = null;
			using (IDestination destination = session.GetQueue(queue))
			using (IMessageConsumer consumer = session.CreateConsumer(destination, selector))
			{
				IMessage message;
				while ((message = consumer.Receive(TimeSpan.FromMilliseconds(2000))) == null)
					Console.WriteLine("Receive timeout");
				ret = message as ITextMessage;
			}
			return ret;
		}

		private FlightDetailsInfoResponse AggregateMessage(Dictionary<int, Object> messages)
		{
			FlightDetailsInfoResponse ret = new FlightDetailsInfoResponse();
			foreach(Object o in messages.Values)
			{
				if(o.GetType() == typeof(Flight))
				{
					ret.flight = (Flight)o;
				}else if(o.GetType() == typeof(Passenger))
				{
					ret.passenger = (Passenger)o;
				}else if(o.GetType() == typeof(Luggage))
				{
					if (ret.luggageList == null)
					{
						ret.luggageList = new List<Luggage>();
					}
					ret.luggageList.Add((Luggage)o);
				}
				else
				{
					throw new ArgumentOutOfRangeException();
				}
			}
			return ret;
		} 

		public FlightDetailsInfoResponse Receive(string queue)
		{
			var maxPartitions = 0;
			var partitionId = 0;
			var messageList = new Dictionary<int, Object>();
			Uri connecturi = new("activemq:tcp://localhost:61616");

			IConnectionFactory factory = new ConnectionFactory(connecturi);
			using (IConnection connection = factory.CreateConnection())
			using (ISession session = connection.CreateSession())
			{
				connection.Start();
				var textMessage = ReceiveMessage(queue, session);
				if (textMessage.Properties["partitionId"] != null)
					partitionId = int.Parse(textMessage.Properties["partitionId"].ToString());

				if (textMessage.Properties["maxPartitions"] != null)
					maxPartitions = int.Parse(textMessage.Properties["maxPartitions"].ToString());

				String messageType = (String)textMessage.Properties["objectType"];
				messageList.Add(partitionId, Newtonsoft.Json.JsonConvert.DeserializeObject(textMessage.Text, Type.GetType(messageType)));
				Console.WriteLine("Received message with GroupID:" + textMessage.Properties["GroupID"] + ", and partitionId=" + partitionId + " out of " + maxPartitions);
				if (textMessage.Properties["GroupID"] != null && maxPartitions > 1)
				{
					for (int i = 1; i < maxPartitions; i++)
					{
						textMessage = ReceiveMessage(queue, session, "GroupID='" + textMessage.Properties["GroupID"] + "'");
						messageType = (String)textMessage.Properties["objectType"];
						partitionId = int.Parse(textMessage.Properties["partitionId"].ToString());
						messageList.Add(i, Newtonsoft.Json.JsonConvert.DeserializeObject(textMessage.Text, Type.GetType(messageType)));
						Console.WriteLine("Received message with GroupID:" + textMessage.Properties["GroupID"] + ", and partitionId=" + partitionId + " out of " + maxPartitions);
					}
				}
			}
			return AggregateMessage(messageList);
		}

	}
}
