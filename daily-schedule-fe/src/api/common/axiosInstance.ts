import axios from "axios";

export const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL, // 팀원이 정한 환경변수 사용
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true, // 필요한 경우 유지
});

// 요청 보낼 때마다 토큰 가로채서 심어주기
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("accessToken");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

// 응답 에러 처리 (토큰 만료 시 로그인 페이지로 등)
axiosInstance.interceptors.response.use(
  (response) => response,
  (error) => {
    return Promise.reject(error);
  },
);
