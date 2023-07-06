package vn.com.hugio.product.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.entity.BaseEntity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@Table(name = "PRODUCT_DETAIL")
public class ProductDetail extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -2062120580227342477L;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
    private Product product;

    @Column(name = "DETAIL_KEY")
    private String key;

    @Column(name = "DETAIL_VALUE")
    private String value;

    public ProductDetail(Product product, String key, String value) {
        this.product = product;
        this.key = key;
        this.value = value;
    }

    @Builder
    public ProductDetail(Long id, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, Product product, String key, String value) {
        super(id, active, createdAt, updatedAt, createdBy, updatedBy);
        this.product = product;
        this.key = key;
        this.value = value;
    }
}
