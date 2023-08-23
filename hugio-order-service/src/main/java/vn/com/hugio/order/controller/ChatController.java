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

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ChatController {

    private final ChatGPT chatGPT;

    @PostMapping
    public ResponseType<String> chat(@RequestBody RequestType<String> request) {
        try {
            return ResponseType.ok(this.chatGPT.chatGPT(request.getRequest(), 1000));
        } catch (Exception e) {
            LOG.exception(e);
            return ResponseType.ok("An error has been occurred");
        }
    }

}
