package daily_schedule.daily_schedule_be.domain.today.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * [일정 결과 엔티티]
 * 'schedule_result' 테이블과 매핑됨.
 * 사용자가 '시작' 버튼을 누르면 real_start_time이 기록되고,
 * '종료' 버튼을 누르면 real_end_time이 기록됨.
 */
@Entity
@Table(name = "schedule_result") // 연결된 테이블 이름
@Getter
@Setter
public class ScheduleResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // (PK)

    // 실제 시작 시간 (DB에 저장될 때 소수점(초 단위 미만) 제거)
    @Column(name = "real_start_time", columnDefinition = "DATETIME(0)")
    private LocalDateTime realStartTime;

    // 실제 종료 시간
    @Column(name = "real_end_time", columnDefinition = "DATETIME(0)")
    private LocalDateTime realEndTime;

    // 완료 여부 (true면 할 일 완료)
    @Column(name = "isFinished")
    private boolean isFinished = false; // 완료 여부

    // 새 일정을 만들 때, 빈 결과지도 같이 만들어서 연결해주는 팩토리 메서드
    public static ScheduleResult createNewResult() {
        ScheduleResult result = new ScheduleResult();
        result.setFinished(false);
        return result;
    }

    /**
     * [비즈니스 로직] 실제 수행 시간을 '분(Minute)' 단위로 계산
     * <p>
     * '어제' 화면에서 분석할 때 사용됨
     */
    public long calculateRealDuration() {
        // 시작이나 종료 시간이 없으면 0분
        if (this.realStartTime == null || this.realEndTime == null) {
            return 0;
        }

        // Java Time API를 이용해 두 시간 사이의 차이를 분 단위로 반환
        return Duration.between(this.realStartTime, this.realEndTime)
                .toMinutes();
    }
}