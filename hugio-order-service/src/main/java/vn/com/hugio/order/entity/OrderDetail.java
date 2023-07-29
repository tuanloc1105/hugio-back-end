package vn.com.hugio.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.entity.BaseEntity;

import java.io.Serial;

@NoArgsConstructor
@Data
@Entity
@Table(name = "USERS")
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
public class OrderDetail extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -4542143539459709114L;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ID", nullable = false)
    private Order order;

    @Column(name = "PRODUCT_UID")
    private String productUid;

    @Column(name = "QUANTITY")
    private Integer quantity;
}
