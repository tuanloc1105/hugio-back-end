package vn.com.hugio.order.service;

import vn.com.hugio.common.pagable.PagableRequest;
import vn.com.hugio.common.pagable.PageResponse;
import vn.com.hugio.order.dto.OrderDto;
import vn.com.hugio.order.dto.SaleStatisticDto;
import vn.com.hugio.order.request.PlaceOrderRequest;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    void placeOrder(PlaceOrderRequest request);

    PageResponse<OrderDto> allOrder(PagableRequest request);

    List<SaleStatisticDto> salesStatisticsByCustomer(LocalDate date);
}
