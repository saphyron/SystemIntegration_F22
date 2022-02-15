import example.org.HelloActiveMq;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

@EnableJms
public class TestMq {


    private JmsTemplate jmsTemplate;

    @Before
    public void before() {
        ConfigurableApplicationContext context = SpringApplication.run(HelloActiveMq.class);
        jmsTemplate = context.getBean(JmsTemplate.class);
    }


    /* Setup an active MQ connection and send a test message */
    @Test
    public void testMq() {
        jmsTemplate.convertAndSend("test.queue", "Hello ActiveMQ");
        String ret = jmsTemplate.receiveAndConvert("test.queue").toString();
        assert(ret.compareTo("Hello ActiveMQ") == 0);
    }

    

}
