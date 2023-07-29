package vn.com.hugio.order.service;

import vn.com.hugio.order.request.PlaceOrderRequest;

public interface OrderService {
    void placeOrder(PlaceOrderRequest request);
}
