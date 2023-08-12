package vn.com.hugio.order.service.impl;

import com.google.common.util.concurrent.AtomicDouble;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.com.hugio.common.pagable.PagableRequest;
import vn.com.hugio.common.pagable.PageLink;
import vn.com.hugio.common.pagable.PageResponse;
import vn.com.hugio.common.service.BaseService;
import vn.com.hugio.order.dto.OrderDetailDto;
import vn.com.hugio.order.dto.OrderDto;
import vn.com.hugio.order.dto.ProductDto;
import vn.com.hugio.order.entity.Order;
import vn.com.hugio.order.entity.OrderDetail;
import vn.com.hugio.order.entity.repository.OrderRepo;
import vn.com.hugio.order.request.PlaceOrderRequest;
import vn.com.hugio.order.service.OrderDetailService;
import vn.com.hugio.order.service.OrderService;
import vn.com.hugio.order.service.grpc.ProductServiceGrpcClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderServiceImpl extends BaseService<Order, OrderRepo> implements OrderService {

    private final ProductServiceGrpcClient productServiceGrpcClient;
    private final OrderDetailService orderDetailService;

    public OrderServiceImpl(OrderRepo repository,
                            ProductServiceGrpcClient productServiceGrpcClient,
                            OrderDetailService orderDetailService) {
        super(repository);
        this.productServiceGrpcClient = productServiceGrpcClient;
        this.orderDetailService = orderDetailService;
    }

    @Override
    public void placeOrder(PlaceOrderRequest request) {
        Order order = Order.builder()
                .totalPrice(0D)
                .build();
        order = this.save(order);
        AtomicReference<Order> atomicReferenceOrder = new AtomicReference<>(order);
        AtomicDouble atomicTotalPrice = new AtomicDouble(0D);
        List<OrderDetail> details = new ArrayList<>();
        request.getOrderInformation().forEach(detail -> {
            ProductDto dto = productServiceGrpcClient.getDetail(detail.getProductUid());
            atomicTotalPrice.set((atomicTotalPrice.get() + dto.getPrice()) * detail.getQuantity());
            OrderDetail orderDetail = orderDetailService.add(atomicReferenceOrder.get(), detail.getProductUid(), detail.getQuantity());
            details.add(orderDetail);
        });
        order.setOrderDetails(details);
        order.setTotalPrice(atomicTotalPrice.get());
        this.save(order);
    }

    @Override
    public PageResponse<OrderDto> allOrder(PagableRequest request) {
        Page<Order> page = this.repository.findAll(PageLink.create(request).toPageable());
        List<OrderDto> dto = page.getContent().stream().map(order -> {
            // lặp qua từng order
            OrderDto dto1 = new OrderDto();
            dto1.setTotalPrice(order.getTotalPrice());
            List<OrderDetailDto> orderDetailDto = new ArrayList<>();
            order.getOrderDetails().forEach(detail -> {
                // lặp qua từng order detail của order
                ProductDto productDto = productServiceGrpcClient.getDetail(detail.getProductUid());
                OrderDetailDto orderDetailDto1 = new OrderDetailDto();
                orderDetailDto1.setQuantity(detail.getQuantity());
                orderDetailDto1.setProductDto(productDto);
                orderDetailDto.add(orderDetailDto1);
            });
            dto1.setOrderDetails(orderDetailDto);
            return dto1;
        }).toList();
        return PageResponse.create(page, dto, true);
    }
}
