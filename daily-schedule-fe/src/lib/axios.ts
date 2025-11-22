import axios from "axios";

// 백엔드 API 기본 주소
const BASE_URL = import.meta.env.VITE_API_BASE_URL;

export const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
  // 인증 쿠키 등을 주고받아야 한다면 true 설정 (현재는 JWT 헤더 방식이라 필수는 아님)
  withCredentials: true,
});

// [요청 인터셉터] 모든 요청을 보내기 전에 실행됨
api.interceptors.request.use(
  (config) => {
    // 로컬 스토리지에서 저장된 토큰을 꺼냄 (로그인 시 'accessToken' 이름으로 저장했다고 가정)
    const token = localStorage.getItem("accessToken");

    // 토큰이 있다면 헤더에 'Bearer {토큰}' 형식으로 추가
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

// [응답 인터셉터] 응답을 받은 후 실행됨 (선택 사항)
api.interceptors.response.use(
  (response) => response,
  (error) => {
    // 토큰 만료 등 401 에러 처리 로직을 여기에 추가할 수 있음
    if (error.response && error.response.status === 401) {
      // 예: 로그인 페이지로 리다이렉트
      // window.location.href = '/login';
    }
    return Promise.reject(error);
  },
);
