import { createContext, useContext } from "react";

type TAuthContext = {
  isLogin: boolean;
  setIsLogin: (state: boolean) => void;
};

export const AuthContext = createContext<TAuthContext | null>(null);

export function useAuthContext() {
  const context = useContext(AuthContext);
  if (context == null) {
    throw new Error("AuthProvider를 찾을 수 없습니다.");
  }

  return context;
}
