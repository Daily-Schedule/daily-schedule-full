package daily_schedule.daily_schedule_be.domain.today.entity;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // (PK)

    @Column(name = "real_start_time")
    private LocalDateTime realStartTime;

    @Column(name = "real_end_time")
    private LocalDateTime realEndTime;

    @Column(name = "isFinished")
    private boolean isFinished;

    //
}
