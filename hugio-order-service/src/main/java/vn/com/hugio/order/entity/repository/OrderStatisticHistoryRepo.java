package vn.com.hugio.order.entity.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.com.hugio.common.entity.repository.BaseRepository;
import vn.com.hugio.order.entity.OrderStatisticHistory;
import vn.com.hugio.order.enums.StatisticType;

import java.time.LocalDate;
import java.util.List;

public interface OrderStatisticHistoryRepo extends BaseRepository<OrderStatisticHistory> {

    @Query("select o from OrderStatisticHistory o where o.statisticDate = :statisticDate")
    List<OrderStatisticHistory> query(@Param("statisticDate") LocalDate statisticDate);

    @Modifying
    @Query("update OrderStatisticHistory set statisticAnswer = :statisticAnswer, statisticQuestion = :statisticQuestion where statisticDate = :statisticDate and statisticType = :statisticType")
    Integer update(
            @Param("statisticAnswer") String statisticAnswer,
            @Param("statisticQuestion") String statisticQuestion,
            @Param("statisticDate") LocalDate statisticDate,
            @Param("statisticType") StatisticType statisticType
    );
}
