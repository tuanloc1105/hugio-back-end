package vn.com.hugio.order.entity.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.hugio.common.entity.repository.BaseRepository;
import vn.com.hugio.order.dto.SaleStatisticDto;
import vn.com.hugio.order.entity.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepo extends BaseRepository<Order> {

    Long countByCreatedAtBetweenAndActiveIsTrue(LocalDateTime start, LocalDateTime end);

    @Query("select new vn.com.hugio.order.dto.SaleStatisticDto(o.customerPhoneNumber, sum(od.quantity)) " +
            "from Order o left join OrderDetail od on o.id = od.order.id " +
            "where o.createdAt between :fromTime and :toTime " +
            "group by o.customerPhoneNumber")
    List<SaleStatisticDto> statisticGroupByCustomerPhoneNumberInDay(
            @Param("fromTime") LocalDateTime fromTime,
            @Param("toTime") LocalDateTime toTime
    );

    @Query("select new vn.com.hugio.order.dto.SaleStatisticDto(o.customerPhoneNumber, sum(od.quantity)) " +
            "from Order o left join OrderDetail od on o.id = od.order.id " +
            "group by o.customerPhoneNumber")
    List<SaleStatisticDto> statisticGroupByCustomerPhoneNumber(
    );

}
