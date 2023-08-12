package vn.com.hugio.order.service;

import vn.com.hugio.common.pagable.PagableRequest;
import vn.com.hugio.common.pagable.PageResponse;
import vn.com.hugio.order.dto.OrderDto;
import vn.com.hugio.order.request.PlaceOrderRequest;

public interface OrderService {
    void placeOrder(PlaceOrderRequest request);

    PageResponse<OrderDto> allOrder(PagableRequest request);
}
