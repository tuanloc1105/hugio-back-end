package vn.com.hugio.inventory.service.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.inventory.request.InventoryRequest;
import vn.com.hugio.inventory.request.ReduceProductQuantityRequest;
import vn.com.hugio.inventory.service.ProductInventoryService;

import java.util.List;

@Component
public class KafkaEventListener {

    private final ObjectMapper objectMapper;
    private final ProductInventoryService productInventoryService;

    public KafkaEventListener(ObjectMapper objectMapper,
                              ProductInventoryService productInventoryService) {
        this.objectMapper = objectMapper;
        this.productInventoryService = productInventoryService;
    }

    @KafkaListener(
            groupId = "${kafka.group-id}",
            topics = "${kafka.topic.create}"
    )
    public void create(@Payload ConsumerRecord<String, String> data, Acknowledgment ack) throws Exception {
        LOG.info(data.toString());
        try {
            InventoryRequest inventoryRequest = this.objectMapper.readValue(data.value(), new TypeReference<InventoryRequest>() {
            });
            this.productInventoryService.create(inventoryRequest);
        } catch (Exception e) {
            LOG.error("[EVENT LISTENER ERROR] {}", e.getMessage());
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(
            groupId = "${kafka.group-id}",
            topics = "${kafka.topic.import_product}"
    )
    public void importProduct(@Payload ConsumerRecord<String, String> data, Acknowledgment ack) throws Exception {
        LOG.info(data.toString());
        try {
            InventoryRequest inventoryRequest = this.objectMapper.readValue(data.value(), new TypeReference<InventoryRequest>() {
            });
            try {
                this.productInventoryService.importProduct(inventoryRequest);
            } catch (InternalServiceException e) {
                this.productInventoryService.create(inventoryRequest);
            }
        } catch (Exception e) {
            LOG.error("[EVENT LISTENER ERROR] {}", e.getMessage());
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(
            groupId = "${kafka.group-id}",
            topics = "${kafka.topic.update_product}"
    )
    public void updateProduct(@Payload ConsumerRecord<String, String> data, Acknowledgment ack) throws Exception {
        LOG.info(data.toString());
        try {
            InventoryRequest inventoryRequest = this.objectMapper.readValue(data.value(), new TypeReference<InventoryRequest>() {
            });
            try {
                this.productInventoryService.updateProduct(inventoryRequest);
            } catch (InternalServiceException e) {
                this.productInventoryService.create(inventoryRequest);
            }
        } catch (Exception e) {
            LOG.error("[EVENT LISTENER ERROR] {}", e.getMessage());
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(
            groupId = "${kafka.group-id}",
            topics = "${kafka.topic.reduce_product_quantity}"
    )
    public void reduceProductQuantity(@Payload ConsumerRecord<String, String> data, Acknowledgment ack) throws Exception {
        LOG.info(data.toString());
        try {
            List<ReduceProductQuantityRequest> request = this.objectMapper.readValue(data.value(), new TypeReference<List<ReduceProductQuantityRequest>>() {
            });
            this.productInventoryService.reduceProductQuantity(request);
        } catch (Exception e) {
            LOG.error("[EVENT LISTENER ERROR] {}", e.getMessage());
        } finally {
            ack.acknowledge();
        }
    }

    @KafkaListener(
            groupId = "${kafka.group-id}",
            topics = "${kafka.topic.recovery_product_quantity}"
    )
    public void recoveryProductQuantity(@Payload ConsumerRecord<String, String> data, Acknowledgment ack) throws Exception {
        LOG.info(data.toString());
        try {
            List<ReduceProductQuantityRequest> request = this.objectMapper.readValue(data.value(), new TypeReference<List<ReduceProductQuantityRequest>>() {
            });
            this.productInventoryService.recoveryProductQuantity(request);
        } catch (Exception e) {
            LOG.error("[EVENT LISTENER ERROR] {}", e.getMessage());
        } finally {
            ack.acknowledge();
        }
    }

}
