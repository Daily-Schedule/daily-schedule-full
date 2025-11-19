package daily_schedule.daily_schedule_be.domain.todo.controller;

import daily_schedule.daily_schedule_be.domain.todo.dto.request.SchedulesRequestDto;
import daily_schedule.daily_schedule_be.domain.todo.dto.response.SchedulesResponseDto;
import daily_schedule.daily_schedule_be.domain.todo.service.SchedulesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 이 클래스가 REST API 컨트롤러임을 알리는 어노테이션
@RestController
@RequiredArgsConstructor
// 해당 컨트롤러의 모든 API가 공통적으로 /api 경로를 가지도록 하는 어노테이션
@RequestMapping("/api/schedules")
public class SchedulesController {
    private final SchedulesService schedulesService;

    // 할 일 등록 API
    // (POST /api/schedules)
    @PostMapping
    public ResponseEntity<SchedulesResponseDto> createSchedule(
            // 요청 Body의 JSON을 DTO로 변환
            @RequestBody SchedulesRequestDto requestDto
    ) {
        // Service를 호출하여 비즈니스 로직 수행
        SchedulesResponseDto responseDto = schedulesService.createSchedule(requestDto);

        // 성공 응답 반환 (HTTP Status 201 CREATED)
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // 내일 할 일 가져오기 API
    // (GET /api/schedules?userId=yoon2013&date=2025-11-14)
    @GetMapping
    public ResponseEntity<List<SchedulesResponseDto>> readSchedule(
            @RequestParam String date
    ) {
        // TODO: 추후 토큰에서 추출하는 것으로 대체
        String userId = "yoon2013";

        // Service를 호출하여 비즈니스 로직 수행
        List<SchedulesResponseDto> responseDtoList = schedulesService.readSchedule(userId, date);

        // 성공 응답 반환
        return ResponseEntity.ok(responseDtoList);
    }

    // 할 일 수정 API
    // (PATCH /api/schedules/{id})
    @PatchMapping
    public ResponseEntity<SchedulesResponseDto> updateSchedule(
            // URL 경로의 id를 변수로 받음
            @RequestParam Long id,
            @RequestBody SchedulesRequestDto requestDto
    ) {
        String userId = "yoon2013";

        // Service를 호출하여 비즈니스 로직 수행
        SchedulesResponseDto responseDto = schedulesService.updateSchedule(id, userId, requestDto);

        // 성공 응답 반환
        return ResponseEntity.ok(responseDto);
    }

    // 할 일 삭제 API
    // (DELETE /api/schedules/{id})
    @DeleteMapping
    public ResponseEntity<SchedulesResponseDto> deleteSchedule(
            // URL 경로의 id를 변수로 받음
            @RequestParam Long id
    ) {
        String userId = "yoon2013";

        // Service를 호출하여 비즈니스 로직 수행
        schedulesService.deleteSchedule(id, userId);

        // 삭제 성공 시 본문 없이 204 No Content 반환
        return ResponseEntity.noContent().build();
    }
}
