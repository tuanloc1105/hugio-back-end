package vn.com.hugio.order.service.impl;

import org.springframework.stereotype.Service;
import vn.com.hugio.common.service.BaseService;
import vn.com.hugio.order.entity.Order;
import vn.com.hugio.order.entity.repository.OrderRepo;
import vn.com.hugio.order.service.OrderService;

@Service
public class OrderServiceImpl extends BaseService<Order, OrderRepo> implements OrderService {
    public OrderServiceImpl(OrderRepo repository) {
        super(repository);
    }

    public void placeOrder() {

    }
}
