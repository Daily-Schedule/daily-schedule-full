package daily_schedule.daily_schedule_be.domain.todo.entity;

import daily_schedule.daily_schedule_be.domain.today.entity.ScheduleResult;
import daily_schedule.daily_schedule_be.domain.user.entity.User;
import daily_schedule.daily_schedule_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "schedule") // 테이블 이름 통일
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
// BaseEntity를 상속받아 생성시간/수정시간 자동 관리
public class Schedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    // columnDefinition을 사용해 MySQL의 DATETIME(0)으로 강제 설정 (소수점 제거)
    @Column(nullable = false, columnDefinition = "DATETIME(0)")
    private LocalDateTime startTime;

    // columnDefinition을 사용해 MySQL의 DATETIME(0)으로 강제 설정 (소수점 제거)
    @Column(nullable = false, columnDefinition = "DATETIME(0)")
    private LocalDateTime endTime;

    // [핵심] 팀원 코드(Long userId) 대신 본인 코드(User 객체) 사용
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // [핵심] 팀원 코드(Long scheduleResultId) 대신 본인 코드(Result 객체) 사용
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "schedule_result_id")
    private ScheduleResult scheduleResult;

    // 변경(Update) 로직
    public void update(String content, LocalDateTime startTime,
                       LocalDateTime endTime) {
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}