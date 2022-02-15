package example.org.configuration;


import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.jms.admin.RMQConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfiguration {
        @Bean
        public ConnectionFactory rabbitConnectionFactory() {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setUsername("user");
            connectionFactory.setPassword("password");
            connectionFactory.setVirtualHost("/");
            connectionFactory.setHost("localhost");
            connectionFactory.setPort(5672);
            return connectionFactory;
        }
}
