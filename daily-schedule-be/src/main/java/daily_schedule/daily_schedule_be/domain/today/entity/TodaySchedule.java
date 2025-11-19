package daily_schedule.daily_schedule_be.domain.today.entity;

import daily_schedule.daily_schedule_be.domain.user.entity.User;
import daily_schedule.daily_schedule_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * schedule_detail 테이블과 1:1로 연결됨
 * '내일 일정 API'가 이 엔티티를 생성
 * '오늘 일정 API'는 이 엔티티를 '조회(GET)'하여 목록을 보여줌
 */
@Entity // Spring에게 DB 테이블과 연결된다고 알려줌
@Table(name = "schedule_detail") // 연결될 테이블의 이름은 'schedule_detail'
@Getter // (Lombok) 이 엔티티의 정보(변수)를 꺼내는 Get 함수를 자동으로 생성
@Setter // (Lombok) 이 엔티티의 정보를 수정하는 Set 함수를 자동으로 생성
public class TodaySchedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB가 알아서 1씩 증가시킴
    private Long id; // (PK)

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime; // 계획된 시작 시간

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;  // 계획된 종료 시간

    @Column(nullable = false)
    private String content; // 일정 내용

    /**
     * '일정 결과' 엔티티와의 1:1 관계 매핑
     * '일정 계획' 1개는 '일정 결과' 1개와 짝을 이룸
     * <p>
     * 이 '계획' 엔티티는 'schedule_result_id'라는 FK(외래 키) 컬럼을 통해
     * 'ScheduleResult' 엔티티와 연결(Join)
     */
    @OneToOne(fetch = FetchType.LAZY) // 1:1 연결, 필요할 때만 불러옴
    @JoinColumn(name = "schedule_result_id")
    private ScheduleResult scheduleResultId; // 연결된 '일정 결과' 엔티티의 객체

    /**
     * 'user' (사용자) 엔티티와의 N:1 연결
     * 여러 개(N)의 일정은 한 명(1)의 사용자에게 속한다.
     * 이 일정은 '어떤 사용자'의 것인가?를 알려줌
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
