package vn.com.hugio.order.service;

import vn.com.hugio.order.entity.Order;
import vn.com.hugio.order.entity.OrderDetail;

public interface OrderDetailService {
    OrderDetail add(Order order, String productUid, Integer quantity);
}
