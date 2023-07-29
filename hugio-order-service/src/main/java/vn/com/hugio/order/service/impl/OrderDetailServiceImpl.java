package vn.com.hugio.order.service.impl;

import org.springframework.stereotype.Service;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.service.BaseService;
import vn.com.hugio.order.entity.Order;
import vn.com.hugio.order.entity.OrderDetail;
import vn.com.hugio.order.entity.repository.OrderDetailRepo;
import vn.com.hugio.order.service.OrderDetailService;

@Service
public class OrderDetailServiceImpl extends BaseService<OrderDetail, OrderDetailRepo> implements OrderDetailService {
    public OrderDetailServiceImpl(OrderDetailRepo repository) {
        super(repository);
    }

    public void add(Order order, String productUid, Long quantity) {
        if (order == null || order.getId() == null) {
            throw new InternalServiceException(ErrorCodeEnum.FAILURE.getCode(), "An error occurred when save a new order detail. order or order.id is null");
        }
        if (productUid == null || productUid.trim().isEmpty() || quantity == null || quantity == 0) {
            throw new InternalServiceException(
                    ErrorCodeEnum.FAILURE.getCode(),
                    "An error occurred when save a new order detail. Values of productUid and quantity is wrong"
            );
        }
        this.repository.save(
                OrderDetail.builder()
                        .order(order)
                        .productUid(productUid)
                        .quantity(quantity)
                        .build()
        );
    }
}
