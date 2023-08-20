package vn.com.hugio.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vn.com.hugio.order.entity.Order;
import vn.com.hugio.order.entity.repository.OrderRepo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@EnableScheduling
@Service
@RequiredArgsConstructor
public class StatisticScheduler {

    private final static String CRON_RUN_EVERY_LAST_MONTH_AT_22 = "0 0 22 28-31 * *";
    private final static String CRON_RUN_END_OF_DAY = "0 0 22 * * *";
    private final OrderRepo orderRepo;

    @Scheduled(cron = CRON_RUN_EVERY_LAST_MONTH_AT_22)
    public void runEOMAt22() {
    }

    @Scheduled(cron = CRON_RUN_END_OF_DAY)
    public void runEOD() {
        StringBuilder question = new StringBuilder("Tôi có dữ liệu bán hàng trong hôm nay như sau:");
        List<Order> orders = this.orderRepo.getOrderEOD(
                LocalDate.now().atTime(LocalTime.MIN),
                LocalDate.now().atTime(LocalTime.MAX)
        );
        if (orders.isEmpty()) {
            return;
        }
        orders.forEach(o -> {
            question.append("\n\t- Đơn hàng ")
                    .append(o.getOrderCode())
                    .append(" được mua bởi khách hàng ")
                    .append(o.getCustomerName())
                    .append(" có số điện thoại ")
                    .append(o.getCustomerPhoneNumber())
                    .append(" có tổng giá là ")
                    .append(o.getTotalPrice())
                    .append(" VNĐ");
        });
        System.out.println(question);
    }

}
