package daily_schedule.daily_schedule_be.domain.today.controller;

import daily_schedule.daily_schedule_be.domain.today.entity.TodaySchedule;
import daily_schedule.daily_schedule_be.domain.today.service.TodayScheduleService;
import daily_schedule.daily_schedule_be.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * '오늘 일정' 관련 API 요청을 처리하는 컨트롤러
 * <p>
 * {@link TodayScheduleService}를 통해 비즈니스 로직을 호출
 *
 * @RestController : 이 클래스가 RESTful API의 컨트롤러임을 나타냄 (JSON 응답)
 * @RequestMapping("/api/schedules") : 이 컨트롤러의 모든 API는 '/api/schedules' 기본
 * 경로를 가짐
 * @RequiredArgsConstructor : final로 선언된 필드(Service)를 자동으로 주입(DI)
 */
@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class TodayScheduleController {
    /**
     * 실제 비즈니스 로직을 처리하는 Service
     * Controller는 Service를 알고 있어야(의존해야) 함
     */
    private final TodayScheduleService todayScheduleService;

    /**
     * 특정 날짜의 일정 목록을 조회하는 API (GET /api/schedules?date=YYYY-MM-DD)
     *
     * @param date (입력) URL 쿼리 파라미터(?date=...)로 전달되는 날짜
     * @return {@link TodaySchedule} 엔티티 목록을 포함한 {@link ResponseEntity}
     * @DateTimeFormat (iso = DateTimeFormat.ISO.DATE) "YYYY-MM-DD" 형식의 문자열을
     * LocalDate 객체로 변환
     */
    @GetMapping
    public ResponseEntity<List<TodaySchedule>> getSchedulesByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        // @TODO 미나님 회원/인증 기능이 완성되면, 실제 로그인한 사용자 객체를 가져와 tempUser 자리에 삽입

        User tempUser = null; // (임시 - 실제 User 객체 주입 필요)

        // Service에게 "이 사용자의, 이 날짜의 일정 목록을 찾아주십쇼"라고 시킴
        List<TodaySchedule> schedules = todayScheduleService.getSchedulesByDate(
                tempUser, date);

        // Service가 찾아온 '일정 목록'을 프론트엔드에게 성공(OK) 상태와 함께 반환
        // 현재는 DB에 데이터가 없어 빈 배열 반환!!
        return ResponseEntity.ok(schedules);
    }

    /**
     * 특정 일정의 '시작'을 기록하는 API (PATCH /api/schedules/{scheduleId}/start
     *
     * @param scheduleId (입력) URL 경로에서 추출한 일정의 고유 ID
     * @return "일정이 시작되었습니다." 성공 메시지를 포함한 {@link ResponseEntity}
     * @PathVariable : URL 경로의 일부(scheduleId)를 파라미터로 가져옴
     */
    @PatchMapping("/{scheduleId}/start")
    public ResponseEntity<String> startSchedule(@PathVariable Long scheduleId) {
        // Service에게 "이 ID의 일정을 '시작' 처리 해주십쇼"라고 시킴
        todayScheduleService.startSchedule(scheduleId);

        return ResponseEntity.ok("일정이 시작되었습니다.");
    }
}
