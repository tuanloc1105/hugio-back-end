package vn.com.hugio.common.gpt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Turbo16kBaseRequest {
    private String model;
    private List<Messages> messages;
}
