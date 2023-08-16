package vn.com.hugio.order.entity.repository;

import vn.com.hugio.common.entity.repository.BaseRepository;
import vn.com.hugio.order.entity.Order;

import java.time.LocalDateTime;

public interface OrderRepo extends BaseRepository<Order> {

    Long countByCreatedAtBetweenAndActiveIsTrue(LocalDateTime start, LocalDateTime end);

}
