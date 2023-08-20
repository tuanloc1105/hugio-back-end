package vn.com.hugio.order.service.impl;

import com.google.common.util.concurrent.AtomicDouble;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.hugio.common.exceptions.ErrorCodeEnum;
import vn.com.hugio.common.exceptions.InternalServiceException;
import vn.com.hugio.common.pagable.PagableRequest;
import vn.com.hugio.common.pagable.PageLink;
import vn.com.hugio.common.pagable.PageResponse;
import vn.com.hugio.common.service.BaseService;
import vn.com.hugio.common.service.CurrentUserService;
import vn.com.hugio.common.utils.StringUtil;
import vn.com.hugio.order.dto.OrderDetailDto;
import vn.com.hugio.order.dto.OrderDto;
import vn.com.hugio.order.dto.ProductDto;
import vn.com.hugio.order.dto.SaleStatisticDto;
import vn.com.hugio.order.dto.StatisticDto;
import vn.com.hugio.order.dto.TotalSaleEachDayDto;
import vn.com.hugio.order.entity.Order;
import vn.com.hugio.order.entity.OrderDetail;
import vn.com.hugio.order.entity.repository.OrderRepo;
import vn.com.hugio.order.enums.OrderStatus;
import vn.com.hugio.order.request.PlaceOrderRequest;
import vn.com.hugio.order.request.StatisticRequest;
import vn.com.hugio.order.request.value.OrderInformation;
import vn.com.hugio.order.service.OrderDetailService;
import vn.com.hugio.order.service.OrderService;
import vn.com.hugio.order.service.grpc.InventoryServiceGrpcClient;
import vn.com.hugio.order.service.grpc.ProductServiceGrpcClient;
import vn.com.hugio.order.service.kafka.KafkaProductService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Transactional(rollbackFor = {Exception.class, Throwable.class, RuntimeException.class, Error.class})
public class OrderServiceImpl extends BaseService<Order, OrderRepo> implements OrderService {

    private final ProductServiceGrpcClient productServiceGrpcClient;
    private final OrderDetailService orderDetailService;
    private final InventoryServiceGrpcClient inventoryServiceGrpcClient;
    private final KafkaProductService kafkaProductService;
    private final CurrentUserService currentUserService;

    public OrderServiceImpl(OrderRepo repository,
                            ProductServiceGrpcClient productServiceGrpcClient,
                            OrderDetailService orderDetailService,
                            InventoryServiceGrpcClient inventoryServiceGrpcClient,
                            KafkaProductService kafkaProductService,
                            CurrentUserService currentUserService) {
        super(repository);
        this.productServiceGrpcClient = productServiceGrpcClient;
        this.orderDetailService = orderDetailService;
        this.inventoryServiceGrpcClient = inventoryServiceGrpcClient;
        this.kafkaProductService = kafkaProductService;
        this.currentUserService = currentUserService;
    }

    @Override
    public void placeOrder(PlaceOrderRequest request) {
        if (
                (StringUtils.isNotEmpty(request.getCustomerName()) && StringUtils.isEmpty(request.getCustomerPhoneNumber())) ||
                        (StringUtils.isNotEmpty(request.getCustomerPhoneNumber()) && StringUtils.isEmpty(request.getCustomerName()))
        ) {
            throw new InternalServiceException(ErrorCodeEnum.FORMAT_ERROR, "full customer info must be provide. can not be non empty and empty");
        }
        //this.kafkaProductService.send(request.getOrderInformation(), "inventory_reduce_product_quantity");
        //this.inventoryServiceGrpcClient.reduceProductQuantity(request.getOrderInformation());
        Long numberOfOrderInDay = this.repository.countByCreatedAtBetweenAndActiveIsTrue(LocalDate.now().atTime(LocalTime.MIN), LocalDate.now().atTime(LocalTime.MAX));
        if (numberOfOrderInDay > 9999) {
            throw new InternalServiceException(ErrorCodeEnum.FAILURE, "max order in a day");
        }
        Order order = Order.builder()
                .totalPrice(0D)
                .customerPhoneNumber(request.getCustomerPhoneNumber())
                .customerName(request.getCustomerName())
                .orderCode(StringUtil.addZeroLeadingNumber(numberOfOrderInDay + 1, "HUGIO" + LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"))))
                .build();
        order = this.repository.save(order);
        AtomicReference<Order> atomicReferenceOrder = new AtomicReference<>(order);
        AtomicDouble atomicTotalPrice = new AtomicDouble(0D);
        List<OrderDetail> details = new ArrayList<>();
        request.getOrderInformation().forEach(detail -> {
            ProductDto dto = productServiceGrpcClient.getDetail(detail.getProductUid());
            atomicTotalPrice.set((atomicTotalPrice.get() + dto.getPrice()) * detail.getQuantity());
            OrderDetail orderDetail = orderDetailService.add(atomicReferenceOrder.get(), detail.getProductUid(), detail.getQuantity());
            details.add(orderDetail);
        });
        order.setOrderDetails(details);
        order.setTotalPrice(atomicTotalPrice.get());
        order.setOrderStatus(OrderStatus.PENDING);
        this.repository.save(order);
    }

    @Override
    public void confirmOrder(String orderCode) {
        var order = this.repository.findByOrderCodeAndActiveIsTrue(orderCode).orElseThrow(() -> new InternalServiceException(ErrorCodeEnum.NOT_EXISTS));
        Integer result = this.repository.updateOrderStatus(
                OrderStatus.DONE,
                this.currentUserService.getUsername(),
                LocalDateTime.now(),
                orderCode,
                OrderStatus.PENDING
        );
        if (result == null || result == 0) {
            throw new InternalServiceException(ErrorCodeEnum.NOT_EXISTS.getCode(), "can not cancel order");
        }
        List<OrderInformation> orderInformations = order.getOrderDetails()
                .stream()
                .map(o -> new OrderInformation(o.getProductUid(), o.getQuantity()))
                .toList();
        this.inventoryServiceGrpcClient.reduceProductQuantity(orderInformations);
    }

    @Override
    public PageResponse<OrderDto> allOrder(PagableRequest request) {
        Page<Order> page = this.repository.findByActiveIsTrue(PageLink.create(request).toPageable());
        List<OrderDto> dto = page.getContent().stream().map(order -> {
            // lặp qua từng order
            OrderDto dto1 = new OrderDto();
            dto1.setTotalPrice(order.getTotalPrice());
            dto1.setOrderCode(order.getOrderCode());
            dto1.setCustomerName(order.getCustomerName());
            dto1.setCustomerPhoneNumber(order.getCustomerPhoneNumber());
            dto1.setCreatedAt(order.getCreatedAt());
            dto1.setCreatedBy(order.getCreatedBy());
            dto1.setUpdatedAt(order.getUpdatedAt());
            dto1.setUpdatedBy(order.getUpdatedBy());
            dto1.setOrderStatus(order.getOrderStatus());
            List<OrderDetailDto> orderDetailDto = new ArrayList<>();
            order.getOrderDetails().forEach(detail -> {
                // lặp qua từng order detail của order
                ProductDto productDto = productServiceGrpcClient.getDetail(detail.getProductUid());
                OrderDetailDto orderDetailDto1 = new OrderDetailDto();
                orderDetailDto1.setQuantity(detail.getQuantity());
                orderDetailDto1.setProductDto(productDto);
                orderDetailDto.add(orderDetailDto1);
            });
            dto1.setOrderDetails(orderDetailDto);
            return dto1;
        }).toList();
        return PageResponse.create(page, dto, true);
    }

    @Override
    public void cancelOrder(String orderCode) {
        var order = this.repository.findByOrderCodeAndActiveIsTrue(orderCode).orElseThrow(() -> new InternalServiceException(ErrorCodeEnum.NOT_EXISTS));
        Integer result = this.repository.updateOrderStatus(
                OrderStatus.CANCELED,
                this.currentUserService.getUsername(),
                LocalDateTime.now(),
                orderCode,
                OrderStatus.PENDING
        );
        if (result == null || result == 0) {
            throw new InternalServiceException(ErrorCodeEnum.NOT_EXISTS.getCode(), "can not cancel order");
        }
        List<OrderInformation> orderInformations = order.getOrderDetails()
                .stream()
                .map(o -> new OrderInformation(o.getProductUid(), o.getQuantity()))
                .toList();
        this.kafkaProductService.send(orderInformations, "recovery_product_quantity");
    }

    @Override
    public List<SaleStatisticDto> salesStatisticsByCustomer(LocalDate date) {
        List<SaleStatisticDto> dto;
        if (date != null) {
            dto = this.repository.statisticGroupByCustomerPhoneNumberInDay(
                    date.atTime(LocalTime.MIN),
                    date.atTime(LocalTime.MAX)
            );
        } else {
            dto = this.repository.statisticGroupByCustomerPhoneNumber();
        }
        return dto;
    }

    @Override
    public StatisticDto statistic(StatisticRequest date) {
        return StatisticDto.builder()
                .totalCancelOrder(this.totalCancelOrder(date.getYear(), date.getMonth()))
                .totalOrderInMonth(this.totalOrderInMonth(date.getYear(), date.getMonth()))
                .totalSaleInMonth(this.totalSaleInMonth(date.getYear(), date.getMonth()))
                .saleEachDay(this.totalOrderEachDay(date.getYear(), date.getMonth()))
                .build();
    }

    /**
     * <code><strong><i><b>java.time.YearMonth methods atDay & atEndOfMonth</b></i></strong></code>
     * <pre>
     * YearMonth yearMonth = YearMonth.of( 2015, 1 );     // 2015-01. January of 2015.
     * LocalDate firstOfMonth = yearMonth.atDay( 1 );     // 2015-01-01
     * LocalDate lastOfMonth = yearMonth.atEndOfMonth();  // 2015-01-31
     * </pre>
     *
     * <b>First day:</b>
     * <pre>
     * Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH);
     * </pre>
     * <b>Last day of month:</b>
     * <pre>
     * Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
     * </pre>
     *
     *
     */
    public Integer totalOrderInMonth(Integer year, Integer month) {
        var current = LocalDate.now();
        YearMonth yearMonth = YearMonth.of(
                year == null || year == 0 || year < 2023 ? current.getYear() : year,
                month == null || month < 1 || month > 12 ? current.getMonthValue() : month
        );
        LocalDateTime firstOfMonth = yearMonth.atDay(1).atTime(LocalTime.MIN);
        LocalDateTime lastOfMonth = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
        return this.repository.totalOrder(firstOfMonth, lastOfMonth).intValue();
    }

    public Double totalSaleInMonth(Integer year, Integer month) {
        var current = LocalDate.now();
        YearMonth yearMonth = YearMonth.of(
                year == null || year == 0 || year < 2023 ? current.getYear() : year,
                month == null || month < 1 || month > 12 ? current.getMonthValue() : month
        );
        LocalDateTime firstOfMonth = yearMonth.atDay(1).atTime(LocalTime.MIN);
        LocalDateTime lastOfMonth = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
        return this.repository.totalSale(firstOfMonth, lastOfMonth);
    }

    public Integer totalCancelOrder(Integer year, Integer month) {
        var current = LocalDate.now();
        YearMonth yearMonth = YearMonth.of(
                year == null || year == 0 || year < 2023 ? current.getYear() : year,
                month == null || month < 1 || month > 12 ? current.getMonthValue() : month
        );
        LocalDateTime firstOfMonth = yearMonth.atDay(1).atTime(LocalTime.MIN);
        LocalDateTime lastOfMonth = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
        return this.repository.totalCancel(firstOfMonth, lastOfMonth).intValue();
    }

    public List<TotalSaleEachDayDto> totalOrderEachDay(Integer year, Integer month) {
        var current = LocalDate.now();
        YearMonth yearMonth = YearMonth.of(
                year == null || year == 0 || year < 2023 ? current.getYear() : year,
                month == null || month < 1 || month > 12 ? current.getMonthValue() : month
        );
        LocalDateTime firstOfMonth = yearMonth.atDay(1).atTime(LocalTime.MIN);
        LocalDateTime lastOfMonth = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
        List<TotalSaleEachDayDto> result = new ArrayList<>();
        List<Object[]> list = this.repository.totalOrderEachDay(firstOfMonth, lastOfMonth);
        list.forEach(o -> {
            TotalSaleEachDayDto dto = new TotalSaleEachDayDto();
            dto.setDate((String) o[0]);
            dto.setTotal((Integer) o[1]);
            result.add(dto);
        });
        return result;
    }



    public static void main(String[] args) {
        System.out.println(Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
    }
}
