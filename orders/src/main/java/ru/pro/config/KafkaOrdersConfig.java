package ru.pro.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(KafkaConfig.class)
public class KafkaOrdersConfig {
}
