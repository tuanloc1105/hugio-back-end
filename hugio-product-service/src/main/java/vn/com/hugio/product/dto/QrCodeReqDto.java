package vn.com.hugio.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class QrCodeReqDto {

    @JsonProperty("frame_name")
    private String frameName;

    @JsonProperty("qr_code_text")
    private String qrCodeText;

    @JsonProperty("image_format")
    private String imageFormat;

    @JsonProperty("qr_code_logo")
    private String qrCodeLogo;

    @JsonProperty("frame_text")
    private String frameText;

    @JsonProperty("frame_icon_name")
    private String frameIconName;

}
