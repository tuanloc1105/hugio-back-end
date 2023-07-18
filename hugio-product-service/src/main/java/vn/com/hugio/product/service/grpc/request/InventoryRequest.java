package vn.com.hugio.product.service.grpc.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InventoryRequest {

    private String productUid;
    private String importedFrom;
    private Long importedQuantity;
    private String importedBy;
    private String note;

}
