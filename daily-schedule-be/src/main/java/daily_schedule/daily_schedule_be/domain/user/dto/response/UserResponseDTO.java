package daily_schedule.daily_schedule_be.domain.user.dto.response;

import lombok.Builder;

@Builder
public record UserResponseDTO(String message, String token, UserTokenDTO tokenInfo, UserInfo userInfo) {


    @Builder
    public record UserTokenDTO(
            String accessToken,
            String refreshToken
    ){}
    @Builder
    public record UserInfo(
            String name
    ){}
}
