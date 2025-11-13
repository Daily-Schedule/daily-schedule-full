package daily_schedule.daily_schedule_be.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * schedule_result 테이블의 Entity
 * '일정 시작' API는 이 테이블의 `real_start_time`을 업데이트해야 함
 */
@Entity
@Table(name = "schedule_result")
@Getter
@Setter
public class ScheduleResult {
    @Id
    private String dailyResultId; // daily_result_id (PK)

    private LocalDateTime realStartTime; // real_start_time
    private LocalDateTime realEndTime; // real_end_time
    private boolean isFinished; // is_finished

    //
}
