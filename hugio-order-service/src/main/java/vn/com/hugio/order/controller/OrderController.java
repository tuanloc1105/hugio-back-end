package vn.com.hugio.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.hugio.common.object.RequestType;
import vn.com.hugio.common.object.ResponseType;
import vn.com.hugio.common.pagable.PagableRequest;
import vn.com.hugio.common.pagable.PageResponse;
import vn.com.hugio.order.dto.OrderDto;
import vn.com.hugio.order.request.PlaceOrderRequest;
import vn.com.hugio.order.service.OrderService;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@CrossOrigin("*")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    public ResponseType<String> order(RequestType<PlaceOrderRequest> request) {
        this.orderService.placeOrder(request.getRequest());
        return ResponseType.ok("ok");
    }

    @PostMapping("/all")
    public ResponseType<PageResponse<OrderDto>> all(RequestType<PagableRequest> request) {
        return ResponseType.ok(
                this.orderService.allOrder(request.getRequest())
        );
    }

}
