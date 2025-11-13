package daily_schedule.daily_schedule_be.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

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
    private String schedule_Id; // schedule_id (PK)

    private LocalTime startTime; // start_time
    private LocalTime endTime; // end_time
    private String content; // content

    private String scheduleResultDailyResultId; // schedule_result_daily_result_id
    private String scheduleDateDateId; //schedule_date_date_Id
    private String userId; // user_id
    
    // @TODO 추후 생성자와 다른 메서드들 추가
}
