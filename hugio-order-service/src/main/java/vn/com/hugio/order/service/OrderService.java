package vn.com.hugio.order.service;

import vn.com.hugio.common.pagable.PagableRequest;
import vn.com.hugio.common.pagable.PageResponse;
import vn.com.hugio.order.dto.OrderDto;
import vn.com.hugio.order.dto.SaleStatisticDto;
import vn.com.hugio.order.dto.StatisticDto;
import vn.com.hugio.order.request.PlaceOrderRequest;
import vn.com.hugio.order.request.StatisticRequest;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    void placeOrder(PlaceOrderRequest request);

    void confirmOrder(String orderCode);

    PageResponse<OrderDto> allOrder(PagableRequest request);

    void cancelOrder(String orderCode);

    List<SaleStatisticDto> salesStatisticsByCustomer(LocalDate date);

    StatisticDto statistic(StatisticRequest date);

    String gptStatisticProductByDay();

    String gptStatisticProductByMonth();

    String statisticProductByMonthNoRecommend();

    String gptStatisticSaleByMonthRecommend();
}
