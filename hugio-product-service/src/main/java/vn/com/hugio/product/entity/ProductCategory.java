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
@Table(name = "PRODUCT_CATEGORY")
public class ProductCategory extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = -5072533522832604499L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_CATEGORY_SEQ")
    @SequenceGenerator(name = "PRODUCT_CATEGORY_SEQ", sequenceName = "PRODUCT_CATEGORY_ID_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false, insertable = true, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "ID", nullable = false)
    private Category category;

    public ProductCategory(Long id, Product product, Category category) {
        this.id = id;
        this.product = product;
        this.category = category;
    }

    @Builder
    public ProductCategory(Long id, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, Long id1, Product product, Category category) {
        super(id, active, createdAt, updatedAt, createdBy, updatedBy);
        this.id = id1;
        this.product = product;
        this.category = category;
    }
}
