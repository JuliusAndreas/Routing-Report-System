package app.navigational.RoutingReportSystem.Configurations;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public Declarables fanoutBindings() {
        Queue topicQueue = new Queue("message-queue", true, false, false);
        TopicExchange topicExchange = new TopicExchange("insertion-exchange", true, false);

        return new Declarables(
                topicQueue,
                topicExchange,
                BindingBuilder.bind(topicQueue).to(topicExchange).with("data.#"));
    }
}
