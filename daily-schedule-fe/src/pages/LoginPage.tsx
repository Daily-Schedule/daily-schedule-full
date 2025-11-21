// import { useState } from "react";
// import { Link, useNavigate } from "react-router-dom";
// import {
//   Card,
//   CardContent,
//   CardHeader,
//   CardTitle,
//   CardDescription,
// } from "../components/ui/card";
// import { Input } from "../components/ui/input";
// import { Label } from "../components/ui/label";
// import { Button } from "../components/ui/button";

// export default function LoginPage() {
//   const navigate = useNavigate();
//   const [email, setEmail] = useState("");
//   const [password, setPassword] = useState("");

//   const handleSubmit = (e: React.FormEvent) => {
//     e.preventDefault();
//     // Demo: just redirect to today page
//     navigate("/today");
//   };

//   return (
//     <div className="min-h-screen flex items-center justify-center p-4">
//       <Card className="w-full max-w-md">
//         <CardHeader className="space-y-1">
//           <CardTitle className="text-2xl font-bold">로그인</CardTitle>
//           <CardDescription>계정에 로그인하여 일정을 관리하세요</CardDescription>
//         </CardHeader>
//         <CardContent>
//           <form onSubmit={handleSubmit} className="space-y-4">
//             <div className="space-y-2">
//               <Label htmlFor="email">이메일</Label>
//               <Input
//                 id="email"
//                 type="email"
//                 placeholder="name@example.com"
//                 value={email}
//                 onChange={(e) => setEmail(e.target.value)}
//                 required
//               />
//             </div>
//             <div className="space-y-2">
//               <Label htmlFor="password">비밀번호</Label>
//               <Input
//                 id="password"
//                 type="password"
//                 value={password}
//                 onChange={(e) => setPassword(e.target.value)}
//                 required
//               />
//             </div>
//             <Button type="submit" className="w-full h-11">
//               로그인
//             </Button>
//           </form>

//           <div className="mt-6 text-center text-sm">
//             <span className="text-muted-foreground">계정이 없으신가요? </span>
//             <Link
//               to="/signup"
//               className="text-foreground hover:underline font-medium"
//             >
//               회원가입
//             </Link>
//           </div>
//         </CardContent>
//       </Card>
//     </div>
//   );
// }

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
import { loginUser } from "../api/authApi";
import { useToast } from "@/hooks/use-toast"; // (선택) 실패 시 메시지용

export default function LoginPage() {
  const navigate = useNavigate();
  const { toast } = useToast(); // (선택) 토스트 메시지

  // id와 password 상태 관리
  const [id, setId] = useState(""); // email -> id로 변경 (백엔드가 id를 씀)
  const [password, setPassword] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      // 2. 실제 백엔드 로그인 API 호출
      const response = await loginUser({ id, password });

      // 3. 받은 토큰을 로컬 스토리지에 저장 (중요!)
      // axios.ts에서 "accessToken"이라는 이름으로 꺼내 쓰도록 설정되어 있음
      localStorage.setItem("accessToken", response.token);

      // (선택) 로그인 성공 메시지
      toast({
        title: "로그인 성공",
        description: "오늘의 일정으로 이동합니다.",
      });

      // 4. 페이지 이동
      navigate("/today");
    } catch (error) {
      console.error("로그인 실패:", error);
      alert("로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.");
    }
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
