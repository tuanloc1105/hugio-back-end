package vn.com.hugio.inventory.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.entity.BaseEntity;
import vn.com.hugio.inventory.enums.ImportBehaviour;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "INVENTORY_LOG")
@EqualsAndHashCode(callSuper=false)
public class InventoryLog extends BaseEntity {

    @Column(name = "PRODUCT_UID", updatable = false)
    private String productUid;

    @Column(name = "IMPORTED_FROM", updatable = false)
    private String importedFrom;

    @Column(name = "IMPORTED_QUANTITY")
    private Long importedQuantity;

    @Column(name = "IMPORTED_BY")
    private String importedBy;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "BEHAVIOUR", nullable = false)
    @Enumerated(EnumType.STRING)
    private ImportBehaviour behaviour;

}
