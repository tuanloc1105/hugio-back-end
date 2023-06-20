package vn.com.hugio.product.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
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

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_DETAIL_SEQ")
    @SequenceGenerator(name = "PRODUCT_DETAIL_SEQ", sequenceName = "PRODUCT_DETAIL_ID_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false, insertable = true, updatable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
    private Product product;

    @Column(name = "DETAIL_KEY")
    private String key;

    @Column(name = "DETAIL_VALUE")
    private String value;

    public ProductDetail(Long id, Product product, String key, String value) {
        this.id = id;
        this.product = product;
        this.key = key;
        this.value = value;
    }

    @Builder
    public ProductDetail(Long id, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, Long id1, Product product, String key, String value) {
        super(id, active, createdAt, updatedAt, createdBy, updatedBy);
        this.id = id1;
        this.product = product;
        this.key = key;
        this.value = value;
    }

}
