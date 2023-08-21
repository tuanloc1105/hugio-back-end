package vn.com.hugio.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.hugio.common.constant.ConsoleColors;
import vn.com.hugio.common.gpt.ChatGPT;
import vn.com.hugio.common.log.LOG;
import vn.com.hugio.order.entity.Order;
import vn.com.hugio.order.entity.OrderStatisticHistory;
import vn.com.hugio.order.entity.repository.OrderRepo;
import vn.com.hugio.order.entity.repository.OrderStatisticHistoryRepo;
import vn.com.hugio.order.enums.StatisticType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@EnableScheduling
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, Throwable.class, RuntimeException.class, Error.class})
public class StatisticScheduler {

    private final static String CRON_RUN_EVERY_LAST_MONTH_AT_22 = "0 0 22 L * ?";
    private final static String CRON_RUN_END_OF_DAY = "0 0 22 ? * *";
    private final OrderRepo orderRepo;
    private final OrderStatisticHistoryRepo orderStatisticHistoryRepo;
    private final ChatGPT chatGPT;

    @Scheduled(cron = CRON_RUN_EVERY_LAST_MONTH_AT_22)
    public void runEOMAt22() {
        LOG.info("run a cron job");
    }

    @Scheduled(cron = CRON_RUN_END_OF_DAY)
    public void runEOD() {
        LOG.info("run a cron job");
        StringBuilder question = new StringBuilder("Tôi có dữ liệu bán hàng trong hôm nay như sau:\n");
        List<Order> orders = this.orderRepo.getOrderEOD(
                LocalDate.now().atTime(LocalTime.MIN),
                LocalDate.now().atTime(LocalTime.MAX)
        );
        if (orders.isEmpty()) {
            return;
        }
        orders.forEach(o -> {
            question.append("\t- Đơn hàng ")
                    .append(o.getOrderCode())
                    .append(" được mua bởi khách hàng ")
                    .append(o.getCustomerName())
                    .append(" có số điện thoại ")
                    .append(o.getCustomerPhoneNumber())
                    .append(" có tổng giá là ")
                    .append(o.getTotalPrice())
                    .append(" VNĐ. Chi tiết đơn hàng bao gồm:\n");
            o.getOrderDetails().forEach(d -> {
                question.append("\t\t+ Mã hàng ")
                        .append(d.getProductUid())
                        .append(" được mua với số lượng ")
                        .append(d.getQuantity())
                        .append("\n");
            });
        });
        question.append("Bạn hãy cho tôi biết các thông tin sau đây:\n")
                .append("\t- Mã hàng nào được mua nhiều nhất trong hôm nay?\n")
                .append("\t- Đơn hàng nào có giá trị cao nhất và được mua bởi khách hàng nào?\nBạn chỉ cần cung cấp cho tôi số liệu cụ thể, không cần giải thích và diễn giải\n");
        try {
            String answer = this.chatGPT.chatGPT(question.toString());
            System.out.println(ConsoleColors.printYellow(question.toString()) + "\n" + ConsoleColors.printGreen(answer));
            Integer updateResult = orderStatisticHistoryRepo.update(answer, question.toString(), LocalDate.now(), StatisticType.ORDER);
            if (updateResult == null || updateResult == 0) {
                OrderStatisticHistory history = new OrderStatisticHistory();
                history.setStatisticQuestion(question.toString());
                history.setStatisticAnswer(answer);
                history.setStatisticDate(LocalDate.now());
                history.setStatisticType(StatisticType.ORDER);
                this.orderStatisticHistoryRepo.save(history);
            }
        } catch (Exception e) {
            LOG.exception(e);
        }
    }

}
