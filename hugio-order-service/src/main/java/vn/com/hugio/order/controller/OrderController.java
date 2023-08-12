package vn.com.hugio.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.hugio.common.object.RequestType;
import vn.com.hugio.common.object.ResponseType;
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

}
