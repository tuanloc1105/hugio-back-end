package vn.com.hugio.order.service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.log.LOG;

import java.util.ArrayList;

@Service
public class KafkaProductService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaProductService(KafkaTemplate<String, Object> kafkaTemplate,
                               ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void send(Object input, String topic, int... partition) {
        LOG.info("[KAFKA] Sending message");
        if (input == null) {
            throw new InternalServiceException(ErrorCodeEnum.FAILURE, "input can not be null");
        }
        try {
            String messageString = this.objectMapper.writeValueAsString(input);
            ProducerRecord<String, Object> message = new ProducerRecord<>(topic, partition.length > 0 ? partition[0] : 0, "key", messageString, new ArrayList<>());
            kafkaTemplate.send(message);
        } catch (Exception e) {
            throw new InternalServiceException(ErrorCodeEnum.FAILURE, e.getMessage());
        }
    }
}
