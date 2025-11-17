package daily_schedule.daily_schedule_be.domain.user.entity;

import daily_schedule.daily_schedule_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder

public class User extends BaseEntity {
    @Id
    @Column(nullable = false, unique = true, length = 20)
    private String id;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 100)
    private String nickname;
}
