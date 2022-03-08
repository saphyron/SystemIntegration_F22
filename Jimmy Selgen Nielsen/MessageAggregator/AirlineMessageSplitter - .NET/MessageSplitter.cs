using Apache.NMS;
using Apache.NMS.ActiveMQ;
using Apache.NMS.Util;
using System;
using System.Collections.Generic;
using System.Text;

namespace AirlineMessageSplitter
{
	class MessageSplitter
	{

		public void SendMessages(string queue, List<Object> messages)
		{
			string connecturi = "activemq:tcp://localhost:61616";
			IConnectionFactory factory = new ConnectionFactory(connecturi);
            using IConnection connection = factory.CreateConnection();
            using ISession session = connection.CreateSession();
            using IDestination destination = session.GetQueue(queue);
            using IMessageProducer producer = session.CreateProducer(destination);
            connection.Start();
            producer.DeliveryMode = MsgDeliveryMode.NonPersistent;
            Guid groupId = Guid.NewGuid();
            int maxPartitions = messages.Count;
            int currentPartition = 0;
            for (int i = maxPartitions - 1; i >= 0; i--)
            {
                Object msg = messages[i];
                string obj = Newtonsoft.Json.JsonConvert.SerializeObject(msg);
                ITextMessage request = session.CreateTextMessage(obj);
                request.Properties["objectType"] = msg.GetType().ToString();
                request.Properties["GroupID"] = groupId.ToString();
                request.Properties["partitionId"] = currentPartition++;
                request.Properties["maxPartitions"] = maxPartitions;

                producer.Send(request);
            }
        }
	}
}
