package vn.com.hugio.product.service.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import vn.com.hugio.common.log.LOG;

@Component
public class ManageChannelConfig {

    @Value("${grpc.server.auth_server}")
    private String serverAddress;

    @Bean
    public ManagedChannel authManagedChannel() {
        LOG.info("CREATE MANAGED CHANNEL AT {}", serverAddress);
        return ManagedChannelBuilder
                .forTarget(serverAddress)
                .defaultLoadBalancingPolicy("round_robin")
                .usePlaintext()
                .build();
    }


}
