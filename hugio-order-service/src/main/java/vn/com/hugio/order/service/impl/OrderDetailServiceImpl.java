package vn.com.hugio.order.service.impl;

import org.springframework.stereotype.Service;
import vn.com.hugio.common.service.BaseService;
import vn.com.hugio.order.entity.OrderDetail;
import vn.com.hugio.order.entity.repository.OrderDetailRepo;
import vn.com.hugio.order.service.OrderDetailService;

@Service
public class OrderDetailServiceImpl extends BaseService<OrderDetail, OrderDetailRepo> implements OrderDetailService {
    public OrderDetailServiceImpl(OrderDetailRepo repository) {
        super(repository);
    }
}
