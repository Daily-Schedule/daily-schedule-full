package daily_schedule.daily_schedule_be.domain.today.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * schedule_result 테이블과 1:1로 연결되는 엔티티
 * <p>
 * '일정 시작/종료' API는 이 엔티티의 정보를 수정(UPDATE)하는 것
 */
@Entity
@Table(name = "schedule_result") // 연결된 테이블 이름
@Getter
@Setter
public class ScheduleResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // (PK)

    // 실제 시작 시간 (소수점 제거)
    @Column(name = "real_start_time", columnDefinition = "DATETIME(0)")
    private LocalDateTime realStartTime;

    // 실제 종료 시간 (소수점 제거)
    @Column(name = "real_end_time", columnDefinition = "DATETIME(0)")
    private LocalDateTime realEndTime;

    /**
     * '일정 종료' API가 호출되면, 이 필드가 'true'로 변환
     * '어제' 탭에서 이 값으로 '완료 여부'를 판단
     */
    @Column(name = "isFinished")
    private boolean isFinished = false; // 완료 여부

    // 'todo' 패키지의 종근님 메서드
    public static ScheduleResult createNewResult() {
        ScheduleResult result = new ScheduleResult();
        result.setFinished(false);
        return result;
    }
}