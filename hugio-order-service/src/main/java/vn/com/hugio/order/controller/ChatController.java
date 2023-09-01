package vn.com.hugio.order.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.hugio.common.gpt.ChatGPT;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.common.object.RequestType;
import vn.com.hugio.common.object.ResponseType;
import vn.com.hugio.order.service.OrderService;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ChatController {

    private final ChatGPT chatGPT;
    private final OrderService orderService;

    @PostMapping
    public ResponseType<String> chat(@RequestBody RequestType<String> request) {
        try {
            if (request.getRequest().equals("ps1")) {
                return ResponseType.ok(this.orderService.gptStatisticProductByDay());
            } else if (request.getRequest().equals("ps2")) {
                return ResponseType.ok(this.orderService.gptStatisticProductByMonth());
            } else if (request.getRequest().equals("sp1")) {
                return ResponseType.ok(this.orderService.statisticProductByMonthNoRecommend());
            } else if (request.getRequest().equals("sp2")) {
                return ResponseType.ok(this.orderService.gptStatisticSaleByMonthRecommend());
            } else {
                return ResponseType.ok(this.chatGPT.chatGPT(request.getRequest(), 1000));
            }
        } catch (Exception e) {
            LOG.exception(e);
            return ResponseType.ok("An error has been occurred");
        }
    }

}
