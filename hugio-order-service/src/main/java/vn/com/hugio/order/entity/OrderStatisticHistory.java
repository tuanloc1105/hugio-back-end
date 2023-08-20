package vn.com.hugio.order.entity;

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
import vn.com.hugio.order.enums.StatisticType;

import java.time.LocalDate;

@NoArgsConstructor
@Data
@Entity
@Table(name = "ORDER_STATISTIC_HISTORY")
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
public class OrderStatisticHistory extends BaseEntity {

    @Column(name = "STATISTIC_QUESTION", columnDefinition = "LONGTEXT")
    private String statisticQuestion;

    @Column(name = "STATISTIC_ANSWER", columnDefinition = "LONGTEXT")
    private String statisticAnswer;

    @Column(name = "STATISTIC_DATE")
    private LocalDate statisticDate;

    @Column(name = "STATISTIC_TYPE")
    @Enumerated(EnumType.STRING)
    private StatisticType statisticType;

}
