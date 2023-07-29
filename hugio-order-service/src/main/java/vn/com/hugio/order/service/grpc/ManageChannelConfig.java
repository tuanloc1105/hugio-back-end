package vn.com.hugio.order.service.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import vn.com.hugio.common.log.LOG;

@Component
public class ManageChannelConfig {

    @Value("${grpc.server.inventory_server}")
    private String inventoryServerAddress;

    @Value("${grpc.server.product_server}")
    private String productServerAddress;

    @Bean
    public ManagedChannel productManagedChannel() {
        LOG.info("CREATE MANAGED CHANNEL AT {}", productServerAddress);
        return ManagedChannelBuilder
                .forTarget(productServerAddress)
                .defaultLoadBalancingPolicy("round_robin")
                .usePlaintext()
                .build();
    }

    @Bean
    public ManagedChannel inventoryManagedChannel() {
        LOG.info("CREATE MANAGED CHANNEL AT {}", inventoryServerAddress);
        return ManagedChannelBuilder
                .forTarget(inventoryServerAddress)
                .defaultLoadBalancingPolicy("round_robin")
                .usePlaintext()
                .build();
    }


}
