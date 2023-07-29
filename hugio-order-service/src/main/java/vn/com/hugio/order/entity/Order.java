package vn.com.hugio.order.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
public class Order extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 690221254719037208L;


}
