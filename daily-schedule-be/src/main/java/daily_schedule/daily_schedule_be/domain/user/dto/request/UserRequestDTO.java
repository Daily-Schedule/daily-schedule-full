package daily_schedule.daily_schedule_be.domain.user.dto.request;
import jakarta.validation.constraints.NotBlank;

public class UserRequestDTO {
    public record Signup(
            @NotBlank String id,
            @NotBlank String password,
            @NotBlank String nickname
    ){
        public String getId() {
            return id;
        }

        public String getPassword() {
            return password;
        }
        public String getNickname() {
            return nickname;
        }
    }
    public record Login(
            @NotBlank String id,
            @NotBlank String password
    ){
        public String getPassword() {
            return password;
        }
    }
}
