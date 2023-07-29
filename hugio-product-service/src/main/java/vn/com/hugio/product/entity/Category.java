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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import vn.com.hugio.common.entity.BaseEntity;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "CATEGORY")
@EqualsAndHashCode(callSuper = false)
public class Category extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 8712926771335467702L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATEGORY_SEQ")
    @SequenceGenerator(name = "CATEGORY_SEQ", sequenceName = "CATEGORY_ID_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false, insertable = true, updatable = false)
    private Long id;

    @Column(name = "CATEGORY_UID", nullable = false, updatable = false)
    private String categoryUid;

    @Column(name = "CATEGORY_NAME", nullable = false)
    private String categoryName;

    @OneToMany(mappedBy = "category", cascade = {CascadeType.ALL}/*, orphanRemoval = true*/)
    private List<ProductCategory> productCategories;

    public Category(Long id, String categoryUid, String categoryName, List<ProductCategory> productCategories) {
        this.id = id;
        this.categoryUid = categoryUid;
        this.categoryName = categoryName;
        this.productCategories = productCategories;
    }

    @Builder
    public Category(Long id, boolean active, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy, Long id1, String categoryUid, String categoryName, List<ProductCategory> productCategories) {
        super(id, active, createdAt, updatedAt, createdBy, updatedBy);
        this.id = id1;
        this.categoryUid = categoryUid;
        this.categoryName = categoryName;
        this.productCategories = productCategories;
    }
}
