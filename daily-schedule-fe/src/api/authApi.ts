import { api } from "@/lib/axios";

// 로그인 요청 시 보낼 데이터 타입
export interface LoginRequest {
  id: string;
  password: string; // 백엔드 DTO는 password지만 form에서는 password로 씀
}

// 로그인 성공 시 받을 데이터 타입 (UserResponseDTO 참고)
export interface LoginResponse {
  message: string;
  token: string;
  // 필요한 경우 tokenInfo, userInfo 등 추가
}

// 로그인 API 호출 함수
export const loginUser = async (data: LoginRequest) => {
  // 19번 줄 수정: 괄호 제거 및 순서 변경
  // await가 api.post(...) 전체 결과를 기다리게 해야 합니다.
  const response = await api.post<LoginResponse>("/user/login", data);

  return response.data;
};
