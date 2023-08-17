package vn.com.hugio.kafka.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import vn.com.hugio.common.log.LOG;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Value("${kafka.server}")
    private String kafkaServer;

    @Value("${kafka.security.protocol}")
    private String kafkaSecurityControl;

    @Value("${kafka.sasl.mechanism}")
    private String kafkaSaslMechanism;

    @Value("${kafka.group-id}")
    private String kafkaGroupId;

    @Bean
    public ConsumerFactory<String, Object> consumerConfigs() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaServer);
        configMap.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configMap.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configMap.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        configMap.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, StringDeserializer.class.getName());
        configMap.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        configMap.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        configMap.put(ConsumerConfig.GROUP_ID_CONFIG, this.kafkaGroupId);
        configMap.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, this.kafkaSecurityControl);
        configMap.put(SaslConfigs.SASL_MECHANISM, this.kafkaSaslMechanism);
        configMap.put("enable.idempotence", "false");
        return new DefaultKafkaConsumerFactory<>(configMap);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(ConsumerFactory<String, Object> consumerConfigs) {
        LOG.info("kafkaListenerContainerFactory");
        ConcurrentKafkaListenerContainerFactory<String, String>
                factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerConfigs);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        return factory;
    }

    @Bean
    public ProducerFactory<String, Object> producerEventMessage() {
        LOG.info("producerEventMessage");
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaServer);
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
        configMap.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, this.kafkaSecurityControl);
        configMap.put(SaslConfigs.SASL_MECHANISM, this.kafkaSaslMechanism);
        return new DefaultKafkaProducerFactory<>(configMap);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate(ProducerFactory<String, Object> producerEventMessage) {
        LOG.info("kafkaTemplate");
        return new KafkaTemplate<>(producerEventMessage);
    }

    /*
    @Bean
    public KafkaAdmin kafkaAdmin() {
        LOG.info("[KAFKA - kafkaAdmin] GENERATE KafkaAdmin");
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaServer);
        configs.put("security.protocol", this.kafkaSecurityControl);
        configs.put("sasl.mechanism", this.kafkaSaslMechanism);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic createInventoryCreate() {
        return new NewTopic("inventory_create", 1, (short) 1);
    }

    @Bean
    public NewTopic createInventoryImportProduct() {
        return new NewTopic("inventory_import_product", 1, (short) 1);
    }

    @Bean
    public NewTopic createInventoryUpdateProduct() {
        return new NewTopic("inventory_update_product", 1, (short) 1);
    }

    @Bean
    public NewTopic createInventoryReduceProductQuantity() {
        return new NewTopic("inventory_reduce_product_quantity", 1, (short) 1);
    }
    */

}
