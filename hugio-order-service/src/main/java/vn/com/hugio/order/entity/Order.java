package vn.com.hugio.order.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.entity.BaseEntity;

import java.io.Serial;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "ORDERS")
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
public class Order extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 690221254719037208L;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "CUSTOMER_PHONE_NUMBER")
    private String customerPhoneNumber;

    @Column(name = "ORDER_CODE")
    private String orderCode;

    @Column(name = "TOTAL_PRICE")
    private Double totalPrice;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<OrderDetail> orderDetails;

}
