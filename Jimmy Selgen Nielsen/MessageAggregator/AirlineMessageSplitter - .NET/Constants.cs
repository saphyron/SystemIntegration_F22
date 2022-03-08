using System;
namespace AirlineMessageSplitter
{
	public class Constants
	{
        private readonly string connectionUri = "activemq:tcp://localhost:61616";
        public Constants()
		{
			
		}

        public string ConnectionUri => connectionUri;
    }
}

