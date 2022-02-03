using System;
using System.Messaging;
using System.Text;
using System.Windows.Forms;

namespace MSMQ
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void Button1_click(object sender, EventArgs e)
        {
            Payment myPayment;
            myPayment.Payor = textBox1.Text;
            myPayment.Payee = textBox2.Text;
            myPayment.Amount = Convert.ToInt32(textBox3.Text);
            myPayment.DueDate = textBox4.Text;

            System.Messaging.Message msg = new System.Messaging.Message();
            msg.Body = myPayment;

            MessageQueue msgQ = new MessageQueue(".\\Private$\\billpay");
            msgQ.Send(msg);
        }

        private void Button2_click(object sender, EventArgs e)
        {
            MessageQueue msgQ = new MessageQueue(".\\Private$\\billpay");

            Payment myPayment = new Payment();
            Object o = new Object();
            System.Type[] arrTypes = new System.Type[2];
            arrTypes[0] = myPayment.GetType();
            arrTypes[1] = o.GetType();
            msgQ.Formatter = new XmlMessageFormatter(arrTypes);
            myPayment = ((Payment)msgQ.Receive().Body);

            StringBuilder sb = new StringBuilder();
            sb.Append("Payment paid to: " + myPayment.Payor);
            sb.Append("\n");
            sb.Append("Paid by: " + myPayment.Payee);
            sb.Append("\n");
            sb.Append("Amount: $" + myPayment.Amount.ToString());
            sb.Append("\n");
            sb.Append("Due Date: " + Convert.ToDateTime(myPayment.DueDate));

            MessageBox.Show(sb.ToString(), "Message Received!");
        }
    }

    public struct Payment
    {
        public string Payor, Payee;
        public int Amount;
        public string DueDate;
    }
}
