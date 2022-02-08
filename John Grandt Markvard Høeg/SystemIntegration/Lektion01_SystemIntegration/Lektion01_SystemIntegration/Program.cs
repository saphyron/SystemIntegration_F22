using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Messaging;

namespace Lektion01_SystemIntegration
{
    class Program
    {
        static void Main(string[] args)
        {

            MessageQueue messageQueue = null;
            string description = "This is a test queue.";
            string message = "This is a test message.";
            string path = @".\Private$\IDG";
            try {
                if (MessageQueue.Exists(path)) {
                    messageQueue = new MessageQueue(path);
                    messageQueue.Label = description;
                }
                else {
                    MessageQueue.Create(path);
                    messageQueue = new MessageQueue(path);
                    messageQueue.Label = description;
                }
                messageQueue.Send(message);
            } catch {
                throw;
            }
            finally {
                messageQueue.Dispose();
            }
        }
    }
}
