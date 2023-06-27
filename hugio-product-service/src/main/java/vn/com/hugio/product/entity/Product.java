package vn.com.hugio.product.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.entity.BaseEntity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Data
@Entity
@Table(name = "PRODUCT")
public class Product extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1134098415121029559L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_SEQ")
    @SequenceGenerator(name = "PRODUCT_SEQ", sequenceName = "PRODUCT_ID_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false, insertable = true, updatable = false)
    private Long id;

    @Column(name = "PRODUCT_UID", nullable = false, updatable = false)
    private UUID productUid;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "RAW_PRODUCT_NAME")
    private String rawProductName;

    @Column(name = "PRICE")
    private Double price;

    @Column(name = "DISCOUNT")
    private Double discount;

    @Column(name = "PRODUCT_DESCRIPTION")
    private String productDescription;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductDetail> productDetails;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductCategory> productCategories;

    public Product(Long id, UUID productUid, String productName, String rawProductName, Double price, Double discount, String productDescription, List<ProductDetail> productDetails, List<ProductCategory> productCategories) {
        this.id = id;
        this.productUid = productUid;
        this.productName = productName;
        this.rawProductName = rawProductName;
        this.price = price;
        this.discount = discount;
        this.productDescription = productDescription;
        this.productDetails = productDetails;
        this.productCategories = productCategories;
    }

    @Builder
    public Product(Long id, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, Long id1, UUID productUid, String productName, String rawProductName, Double price, Double discount, String productDescription, List<ProductDetail> productDetails, List<ProductCategory> productCategories) {
        super(id, active, createdAt, updatedAt, createdBy, updatedBy);
        this.id = id1;
        this.productUid = productUid;
        this.productName = productName;
        this.rawProductName = rawProductName;
        this.price = price;
        this.discount = discount;
        this.productDescription = productDescription;
        this.productDetails = productDetails;
        this.productCategories = productCategories;
    }
}