package daily_schedule.daily_schedule_be.domain.user.controller;

import daily_schedule.daily_schedule_be.domain.user.dto.request.UserRequestDTO;
import daily_schedule.daily_schedule_be.domain.user.dto.response.UserResponseDTO;
import daily_schedule.daily_schedule_be.domain.user.service.UserCommandService;
import daily_schedule.daily_schedule_be.domain.user.service.UserQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated

public class UserController {
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@Valid @RequestBody UserRequestDTO.Signup signup) {
        userCommandService.register(signup);

        // Map을 사용하여 "message" 필드만 포함합니다.
        Map<String, String> response = Collections.singletonMap("message", "회원가입에 성공했습니다.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody UserRequestDTO.Login login) {
        String token = userCommandService.login(login);
        UserResponseDTO response = UserResponseDTO.builder()
                .message("로그인 성공")
                .token(token)
                .build();
        return ResponseEntity.ok(response);
    }


}
