import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
  CardDescription,
} from "../components/ui/card";
import { Input } from "../components/ui/input";
import { Label } from "../components/ui/label";
import { Button } from "../components/ui/button";
// 1. API 함수 임포트
import { PostLogin } from "../api/authApi";
import { useToast } from "@/hooks/use-toast"; // (선택) 실패 시 메시지용
import { useMutation } from "@tanstack/react-query";
import { useAuthContext } from "../context/AuthContext";

export default function LoginPage() {
  const navigate = useNavigate();
  const { toast } = useToast(); // (선택) 토스트 메시지
  const { setIsLogin } = useAuthContext();

  // id와 password 상태 관리
  const [id, setId] = useState(""); // email -> id로 변경 (백엔드가 id를 씀)
  const [password, setPassword] = useState("");

  const { mutate: postLogin } = useMutation({
    mutationFn: PostLogin,
    onSuccess: (response) => {
      console.log("로그인 성공");
      localStorage.setItem("accessToken", response.token);
      setIsLogin(true);
      toast({
        title: "로그인 성공",
        description: "오늘의 일정으로 이동합니다.",
      });
      navigate("/today");
    },
    onError: () => {
      console.error("로그인 실패");
    },
  });
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    postLogin({ id, password });
  };

  return (
    <div className="min-h-screen flex items-center justify-center p-4">
      <Card className="w-full max-w-md">
        <CardHeader className="space-y-1">
          <CardTitle className="text-2xl font-bold">로그인</CardTitle>
          <CardDescription>계정에 로그인하여 일정을 관리하세요</CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="space-y-2">
              {/* 라벨과 input type을 email -> text(아이디)로 변경 */}
              <Label htmlFor="id">아이디</Label>
              <Input
                id="id"
                type="text"
                placeholder="아이디를 입력하세요"
                value={id}
                onChange={(e) => setId(e.target.value)}
                required
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="password">비밀번호</Label>
              <Input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>
            <Button type="submit" className="w-full h-11">
              로그인
            </Button>
          </form>

          <div className="mt-6 text-center text-sm">
            <span className="text-muted-foreground">계정이 없으신가요? </span>
            <Link
              to="/signup"
              className="text-foreground hover:underline font-medium"
            >
              회원가입
            </Link>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
