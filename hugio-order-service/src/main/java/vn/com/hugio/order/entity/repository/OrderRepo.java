package vn.com.hugio.order.entity.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.hugio.common.entity.repository.BaseRepository;
import vn.com.hugio.order.dto.SaleStatisticDto;
import vn.com.hugio.order.entity.Order;
import vn.com.hugio.order.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepo extends BaseRepository<Order> {

    Page<Order> findByActiveIsTrue(Pageable pageable);

    Optional<Order> findByOrderCodeAndActiveIsTrue(String orderCode);

    Long countByCreatedAtBetweenAndActiveIsTrue(LocalDateTime start, LocalDateTime end);

    @Modifying
    @Query("update Order set orderStatus = :orderStatus, updatedBy = :updatedBy, updatedAt = :updatedAt where orderCode = :orderCode and orderStatus = :currentOrderStatus and active = true")
    Integer updateOrderStatus(
            @Param("orderStatus") OrderStatus orderStatus,
            @Param("updatedBy") String updatedBy,
            @Param("updatedAt") LocalDateTime updatedAt,
            @Param("orderCode") String orderCode,
            @Param("currentOrderStatus") OrderStatus currentOrderStatus
    );

    @Query("select new vn.com.hugio.order.dto.SaleStatisticDto(o.customerPhoneNumber, sum(od.quantity)) " +
            "from Order o left join OrderDetail od on o.id = od.order.id " +
            "where o.createdAt between :fromTime and :toTime " +
            "and o.active = true " +
            "group by o.customerPhoneNumber")
    List<SaleStatisticDto> statisticGroupByCustomerPhoneNumberInDay(
            @Param("fromTime") LocalDateTime fromTime,
            @Param("toTime") LocalDateTime toTime
    );

    @Query("select new vn.com.hugio.order.dto.SaleStatisticDto(o.customerPhoneNumber, sum(od.quantity)) " +
            "from Order o left join OrderDetail od on o.id = od.order.id " +
            "and o.active = true " +
            "group by o.customerPhoneNumber")
    List<SaleStatisticDto> statisticGroupByCustomerPhoneNumber(
    );

    @Query("select count(o) from Order o where o.createdAt between :fromDate and :toDate and o.active = true")
    Long totalOrder(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

    @Query("select sum(o.totalPrice) from Order o where o.createdAt between :fromDate and :toDate and o.active = true")
    Double totalSale(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

    @Query("select count(o) from Order o where o.createdAt between :fromDate and :toDate and o.active = false")
    Long totalCancel(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

    @Query(value = "select DATE_FORMAT(CAST(o.CREATED_AT as DATE), '%d-%m-%Y'), count(*) from ORDERS o where o.CREATED_AT between :fromDate and :toDate and o.ACTIVE = 1 group by CAST(o.CREATED_AT as DATE)", nativeQuery = true)
    List<Object[]> totalOrderEachDay(
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate
    );

}
