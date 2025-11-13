package daily_schedule.daily_schedule_be.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * schedule_detail 테이블의 Entity
 *
 */
@Entity
@Table(name = "schedule_detail")
@Getter
@Setter
public class TodaySchedule {
    @Id
    @Column(name = "schedule_id")
    private String schedule_Id; // (PK)

    @Column(name = "start_time")
    private LocalDateTime startTime;
    @Column(name = "end_time")
    private LocalDateTime endTime;
    @Column
    private String content;

    // 다른 테이블과 연결되는 외래 키(FK)들
    @Column(name = "schedule_result_daily_result_id")
    private String scheduleResultDailyResultId;
    @Column(name = "schedule_date_date_Id")
    private String scheduleDateDateId;
    @Column(name = "user_id")
    private String userId;

    // @TODO 추후 생성자와 다른 메서드들 추가
}
