package daily_schedule.daily_schedule_be.domain.user.entity;

import daily_schedule.daily_schedule_be.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder

public class User extends BaseEntity implements UserDetails {
    @Id
    @Column(nullable = false, unique = true, length = 20)
    private String id;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 100)
    private String nickname;

    /**
     * 권한(Role) 반환
     * 지금은 별도의 Role 필드가 없으므로 기본적으로 "ROLE_USER"를 부여한다고 가정합니다.
     * 추후 Role 필드(Enum 등)를 만들면 그 값을 반환하도록 수정하면 됩니다.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    /**
     * 사용자의 고유한 ID(식별자) 반환
     * 중요!!!: Spring Security는 'username'이라는 이름을 쓰지만,
     * 우리 DB는 'id'라는 필드명을 쓰므로 여기서 매핑해줍니다.
     */
    @Override
    public String getUsername() {
        return this.id;
    }

    /**
     * 비밀번호 반환
     * Lombok의 @Getter가 이미 getPassword() 메서드를 만들어주므로
     * 사실 명시적으로 오버라이딩 안 해도 작동하지만, 명확성을 위해 적어두셔도 됩니다.
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    // 계정 만료 여부 (true: 만료 안 됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠김 여부 (true: 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 비밀번호 만료 여부 (true: 만료 안 됨)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 여부 (true: 활성화 됨)
    @Override
    public boolean isEnabled() {
        return true;
    }

}
