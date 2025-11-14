package daily_schedule.daily_schedule_be.domain;

import daily_schedule.daily_schedule_be.dto.request.SchedulesRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

// @Entity: JPA에게 해당 클래스가 DB 테이블과 매핑된다고 알리는 어노테이션
@Entity
@Getter
// @NoArgsConstructor: JPA에게 필요한 기본 생성자
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// AuditingEntityListener: 생성 시간을 자동 기록해주는 역할
@EntityListeners(AuditingEntityListener.class)
public class Schedule {
    // @Id: 해당 필드가 PK임을 알리는 어노테이션
    @Id
    // DB가 ID를 자동으로 증가하도록 생성
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // DB 컬럼
    // NOT NULL -> nullable = false
    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long scheduleResultId;

    // 데이터 생성 시 자동 시간 기록
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // DTO를 받아서 Entity를 생성하는 생성자
    public Schedule(SchedulesRequestDto requestDto, String userId, Long scheduleResultId) {
        this.userId = userId;
        this.scheduleResultId = scheduleResultId;
        this.startTime = requestDto.getStartTime();
        this.endTime = requestDto.getEndTime();
        this.content = requestDto.getContent();
    }

    public void update(SchedulesRequestDto requestDto) {
        this.startTime = requestDto.getStartTime();
        this.endTime = requestDto.getEndTime();
        this.content = requestDto.getContent();
    }
}