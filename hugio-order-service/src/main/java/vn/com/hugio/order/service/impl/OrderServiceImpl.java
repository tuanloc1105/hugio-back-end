package vn.com.hugio.order.service.impl;

import com.google.common.util.concurrent.AtomicDouble;
import org.springframework.stereotype.Service;
import vn.com.hugio.common.service.BaseService;
import vn.com.hugio.order.dto.ProductDto;
import vn.com.hugio.order.entity.Order;
import vn.com.hugio.order.entity.repository.OrderRepo;
import vn.com.hugio.order.request.PlaceOrderRequest;
import vn.com.hugio.order.service.OrderService;
import vn.com.hugio.order.service.grpc.ProductServiceGrpcClient;

@Service
public class OrderServiceImpl extends BaseService<Order, OrderRepo> implements OrderService {

    private final ProductServiceGrpcClient productServiceGrpcClient;

    public OrderServiceImpl(OrderRepo repository,
                            ProductServiceGrpcClient productServiceGrpcClient) {
        super(repository);
        this.productServiceGrpcClient = productServiceGrpcClient;
    }

    public void placeOrder(PlaceOrderRequest request) {
        AtomicDouble atomicTotalPrice = new AtomicDouble(0D);
        request.getOrderInformation().stream().forEach(detail -> {
            ProductDto dto = productServiceGrpcClient.getDetail(detail.getProductUid());
            atomicTotalPrice.set((atomicTotalPrice.get() + dto.getPrice()) * detail.getQuantity());

        });
    }
}
