package vn.com.hugio.inventory.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.entity.BaseEntity;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PRODUCT_INVENTORY")
@EqualsAndHashCode(callSuper=false)
public class ProductInventory extends BaseEntity {

    @Column(name = "PRODUCT_UID", updatable = false)
    private String productUid;

    @Column(name = "QUANTITY")
    private Long quantity;

}
