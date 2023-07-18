package vn.com.hugio.inventory.service.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import vn.com.hugio.common.log.LOG;

@Component
public class ManageChannelConfig {

    @Value("${grpc.server.auth_server}")
    private String authServerAddress;

    @Value("${grpc.server.inventory_server}")
    private String inventoryServerAddress;

    @Bean
    public ManagedChannel authManagedChannel() {
        LOG.info("CREATE MANAGED CHANNEL AT {}", authServerAddress);
        return ManagedChannelBuilder
                .forTarget(authServerAddress)
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
