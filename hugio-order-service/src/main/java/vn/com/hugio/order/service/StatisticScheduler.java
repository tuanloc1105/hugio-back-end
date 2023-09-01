package vn.com.hugio.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.com.hugio.common.log.LOG;

@EnableScheduling
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = {Exception.class, Throwable.class, RuntimeException.class, Error.class})
public class StatisticScheduler {

    private final static String CRON_RUN_EVERY_LAST_MONTH_AT_22 = "0 0 22 L * ?";
    private final static String CRON_RUN_END_OF_DAY = "0 0 22 ? * *";
    private final OrderService orderService;

    @Scheduled(cron = CRON_RUN_EVERY_LAST_MONTH_AT_22)
    public void runEOMAt22() {
        LOG.info("run a cron job");
        this.orderService.gptStatisticSaleByMonthRecommend();
    }

    @Scheduled(cron = CRON_RUN_END_OF_DAY)
    public void runEOD() {
        LOG.info("run a cron job");
        this.orderService.gptStatisticProductByDay();
    }

}
