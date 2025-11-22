package daily_schedule.daily_schedule_be.domain.today.controller;

import daily_schedule.daily_schedule_be.domain.today.dto.response.TodayScheduleResponseDto;
import daily_schedule.daily_schedule_be.domain.today.service.TodayScheduleService;
import daily_schedule.daily_schedule_be.domain.todo.entity.Schedule;
import daily_schedule.daily_schedule_be.domain.user.entity.User;
import daily_schedule.daily_schedule_be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * '오늘 일정' 관련 API 요청을 처리하는 컨트롤러
 * <p>
 * 프론트엔드에서 오는 '오늘 일정' 관련 요청(조회, 시작, 종료, 마감)을 받아 서비스로 넘겨주는 역할
 * <p>
 * {@link TodayScheduleService}를 통해 비즈니스 로직을 호출
 *
 * @RestController : 이 클래스가 RESTful API의 컨트롤러임을 나타냄 (JSON 응답)
 * @RequestMapping("/api/schedules") : 이 컨트롤러의 모든 API는 '/api/schedules' 기본
 * 경로를 가짐
 * @RequiredArgsConstructor : final로 선언된 필드(Service)를 자동으로 주입(DI)
 */
@RestController
@RequestMapping("/api/today-schedules")
@RequiredArgsConstructor
public class TodayScheduleController {
    /**
     * 실제 비즈니스 로직을 처리하는 Service
     * Controller는 Service를 알고 있어야(의존해야) 함
     */
    private final TodayScheduleService todayScheduleService;
    private final UserRepository userRepository;

    /**
     * 오늘 일정 목록 조회 API
     * <p></p>
     * [GET] /api/today-schedules?date=2025-11-22
     *
     * @param date        : URL 쿼리 파라미터로 넘어온 날짜 (String -> LocalDate 자동 변환)
     * @param userDetails : Spring Security가 로그인 토큰(JWT)을 해석해서 넣어준 유저 정보
     */
    @GetMapping
    public ResponseEntity<List<TodayScheduleResponseDto>> getSchedulesByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal UserDetails userDetails) {

        // [보안] 토큰에 담긴 ID로 실제 유저가 DB에 존재하는지 확인
        String userId = userDetails.getUsername();
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // [로직] 서비스에게 "이 유저의 해당 날짜 일정을 다 가져와라" 명령
        List<Schedule> schedules = todayScheduleService.getSchedulesByDate(user,
                date);

        // [변환] DB에서 꺼낸 원본 데이터(Entity)를 프론트엔드용 데이터(DTO)로 변환
        // (Entity에는 민감한 정보가 있을 수 있어 DTO로 변환해서 내보내는 것이 안전함)
        List<TodayScheduleResponseDto> response = schedules.stream()
                .map(TodayScheduleResponseDto::from) // 하나씩 변환
                .collect(Collectors.toList());

        // 엔티티 목록을 반환한 DTO 목록을 반환
        return ResponseEntity.ok(response);
    }

    /**
     * 일정 시작 (타이머 시작) API
     * <p></p>
     * [PATCH] /api/today-schedules/{scheduleId}/start
     *
     * @param scheduleId : URL 경로에 있는 일정 ID (예: /15/start -> 15번 일정)
     */
    @PatchMapping("/{scheduleId}/start")
    public ResponseEntity<String> startSchedule(@PathVariable Long scheduleId) {
        // Service에게 "이 ID의 일정을 '시작' 처리 해주십쇼"라고 시킴
        todayScheduleService.startSchedule(scheduleId);

        return ResponseEntity.ok("일정이 시작되었습니다.");
    }

    /**
     * 일정 종료 (타이머 종료 및 완료) API
     * <p></p>
     * [PATCH] /api/today-schedules/{scheduleId}/end
     */
    @PatchMapping("/{scheduleId}/end")
    public ResponseEntity<String> endSchedule(@PathVariable Long scheduleId) {
        // 서비스에게 "이 일정을 종료(완료) 처리해라" 명령
        todayScheduleService.endSchedule(scheduleId);

        return ResponseEntity.ok(" 일정이 종료되었습니다.");
    }

    /**
     * 하루 마감하기 API
     * <p></p>
     * [POST] /api/today-schedules/finish?date=2025-11-22
     * <p></p>
     * 사용자가 "오늘 하루 끝내기" 버튼을 눌렀을 때 호출
     */
    @PostMapping("/finish")
    public ResponseEntity<String> finishToday(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        todayScheduleService.finishDay(user, date);
        return ResponseEntity.ok("오늘 하루가 마감되었습니다.");
    }

    /**
     * 마감 여부 확인 API
     * <p></p>
     * [GET] /api/today-schedules/is-finished?date=2025-11-22
     * <p></p>
     * 페이지 새로고침 시, 오늘이 이미 마감된 날인지 확인하기 위함
     */
    @GetMapping("/is-finished")
    public ResponseEntity<Boolean> checkDayFinished(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        boolean isFinished = todayScheduleService.isDayFinished(user, date);
        return ResponseEntity.ok(isFinished);
    }
}
