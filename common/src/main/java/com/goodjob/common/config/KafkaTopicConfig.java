package com.goodjob.common.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    private final String QUESTION_TOPIC_NAME = "question-event";

    private final String ADVICE_TOPIC_NAME = "advice-event";
    private final String NUM_PARTITIONS = "3";
    /**
     * 브로커가 셋이므로, 3개의 브로커가 있을 경우 3개의 브로커에 모두 복제해야한다.
     * 그 이상일 경우 의미가 없으며, 그 이하일 경우 브로커가 죽었을 때 데이터가 유실될 수 있다.
     */
    private final String REPLICA_FACTOR = "3";

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    /**
     * broker 를 두개만 설정하였으므로 최소 Replication Factor로 2를 설정하고
     * Partition의 경우 Event 의 Consumer인 WAS를 2대까지만 실행되도록 해두었기 때문에 2로 설정함.
     * 이보다 Partition을 크게 설정한다고 해서 Consume 속도가 빨라지지 않기 때문이다.
     * @return
     */
    @Bean
    public NewTopic questionTopic() {
        return new NewTopic(QUESTION_TOPIC_NAME, Integer.parseInt(NUM_PARTITIONS), Short.parseShort(REPLICA_FACTOR));
    }

    @Bean
    public NewTopic adviceTopic() {
        return new NewTopic(ADVICE_TOPIC_NAME, Integer.parseInt(NUM_PARTITIONS), Short.parseShort(REPLICA_FACTOR));
    }
}
