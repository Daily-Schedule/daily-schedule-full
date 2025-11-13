package daily_schedule.daily_schedule_be.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column( length = 255)
    private String password;

    @Column(nullable = false, length = 100)
    private String nickname;
}
