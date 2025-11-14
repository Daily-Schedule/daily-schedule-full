package daily_schedule.daily_schedule_be.domain.today.entity;

import daily_schedule.daily_schedule_be.domain.user.entity.User;
import daily_schedule.daily_schedule_be.global.entity.BaseEntity;
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
public class TodaySchedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // (PK)

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String content;

    // 다른 테이블과 연결되는 외래 키(FK)들
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_result_id")
    private ScheduleResult scheduleResultId;

    // date 테이블 안쓰는걸로 임시 결정
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "schedule_date_id", nullable = false)
    // private Schedule

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // @TODO 추후 생성자와 다른 메서드들 추가
}
