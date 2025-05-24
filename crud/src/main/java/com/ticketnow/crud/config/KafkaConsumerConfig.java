package com.ticketnow.crud.config;

import com.ticketnow.crud.dto.CouponKafkaDTO;
import com.ticketnow.crud.dto.OrderEmailDTO;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SaslConfigs;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, OrderEmailDTO> EmailConsumerFactory() {
        Map<String, Object> configProps = setupKafkaConsumer();
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "email");
        JsonDeserializer<OrderEmailDTO> deserializer = new JsonDeserializer<>(OrderEmailDTO.class);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("com.ticketnow.order.dto");
        deserializer.setUseTypeMapperForKey(true);
        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(),
                deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderEmailDTO> emailKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderEmailDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(EmailConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, CouponKafkaDTO> CouponConsumerFactory() {
        Map<String, Object> configProps = setupKafkaConsumer();
        JsonDeserializer<CouponKafkaDTO> deserializer = new JsonDeserializer<>(CouponKafkaDTO.class);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "coupon");
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("com.ticketnow.order.dto");
        deserializer.setUseTypeMapperForKey(true);
        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(),
                deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CouponKafkaDTO> CouponKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CouponKafkaDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(CouponConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> stringConsumerFactory() {
        Map<String, Object> configProps = setupKafkaConsumer();
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, "ticket");
        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(),
                new StringDeserializer());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> StringKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(stringConsumerFactory());
        return factory;
    }

    private Map<String, Object> setupKafkaConsumer() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        configProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        configProps.put(SaslConfigs.SASL_MECHANISM, "PLAIN");
        configProps.put(SaslConfigs.SASL_JAAS_CONFIG, "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"user\" password=\"password\";");
        return configProps;
    }
}
