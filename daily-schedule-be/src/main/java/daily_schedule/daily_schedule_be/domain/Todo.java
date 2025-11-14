package daily_schedule.daily_schedule_be.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.date.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

// @Entity: JPA에게 해당 클래스가 DB 테이블과 매핑된다고 알리는 어노테이션
@Entity
@Getter
// @NoArgsConstructor: JPA에게 필요한 기본 생성자
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// AuditingEntityListener: 생성 시간을 자동 기록해주는 역할
@EntityListeners(AuditingEntityListener.class)
public class Todo {
    // @Id: 해당 필드가 PK임을 알리는 어노테이션
    @Id
    // DB가 ID를 자동으로 증가하도록 생성
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    // DB 컬럼
    // NOT NULL -> nullable = false
    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false)
    private String content;

    // 데이터 생성 시 자동 시간 기록
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createAt;

    // DTO를 받아서 Entity를 생성하는 생성자
    public Todo(LocalDateTime startAt, LocalDateTime endAt, String content) {
        this.startAt = startAt;
        this.endAt = endAt;
        this.content = content;
    }
}