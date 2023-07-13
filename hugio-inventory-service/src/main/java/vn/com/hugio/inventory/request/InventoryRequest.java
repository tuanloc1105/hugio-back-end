package vn.com.hugio.inventory.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InventoryRequest {

    private String productUid;
    private String importedFrom;
    private Long importedQuantity;
    private String importedBy;
    private String note;

}
