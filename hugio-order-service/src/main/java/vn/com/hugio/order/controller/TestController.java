package vn.com.hugio.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.hugio.common.object.RequestType;
import vn.com.hugio.common.object.ResponseType;
import vn.com.hugio.common.pagable.PagableRequest;
import vn.com.hugio.common.pagable.PageResponse;
import vn.com.hugio.order.dto.OrderDto;
import vn.com.hugio.order.dto.SaleStatisticDto;
import vn.com.hugio.order.dto.StatisticDto;
import vn.com.hugio.order.request.PlaceOrderRequest;
import vn.com.hugio.order.request.SaleStatisticRequest;
import vn.com.hugio.order.request.StatisticRequest;
import vn.com.hugio.order.service.OrderService;
import vn.com.hugio.order.service.StatisticScheduler;

import java.util.List;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TestController {

    private final StatisticScheduler statisticScheduler;

    @PostMapping()
    public ResponseType<String> test() {
        this.statisticScheduler.runEOMAt22();
        return ResponseType.ok("ok");
    }

}
