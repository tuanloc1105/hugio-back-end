package vn.com.hugio.inventory.entiry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.entity.BaseEntity;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PRODUCT_INVENTORY")
public class ProductInventory extends BaseEntity {

    @Column(name = "PRODUCT_UID", updatable = false)
    private String productUid;

    @Column(name = "QUANTITY")
    private Long quantity;

}
