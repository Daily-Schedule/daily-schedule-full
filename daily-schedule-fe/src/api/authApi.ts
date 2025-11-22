import { axiosInstance } from "./common/axiosInstance";

export const PostRegister = async (data: {
  id: string;
  password: string;
  nickname: string;
}) => {
  console.log("data", data);
  try {
    const response = await axiosInstance.post("/api/user/register", data);
    return response.data;
  } catch (error) {
    console.error("회원가입 실패", error);
  }
};

export const PostLogin = async (data: { id: string; password: string }) => {
  try {
    const response = await axiosInstance.post("/api/user/login", data);
    return response.data;
  } catch (error) {
    console.error("로그인 실패", error);
  }
};
