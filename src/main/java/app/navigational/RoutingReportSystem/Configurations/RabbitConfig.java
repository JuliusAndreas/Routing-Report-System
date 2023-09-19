package app.navigational.RoutingReportSystem.Configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String REPORTS_QUEUE_NAME = "report-queue";
    public static final String REPORTS_EXCHANGE_NAME = "report-exchange";
    @Bean
    public Declarables fanoutBindings() {
        Queue topicQueue = new Queue(REPORTS_QUEUE_NAME, true, false, false);
        TopicExchange topicExchange = new TopicExchange(REPORTS_EXCHANGE_NAME, true, false);

        return new Declarables(
                topicQueue,
                topicExchange,
                BindingBuilder.bind(topicQueue).to(topicExchange).with("data.#"));
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
