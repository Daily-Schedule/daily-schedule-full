import type { PropsWithChildren } from "react";
import { useState } from "react";
import { AuthContext } from "./AuthContext";

export const AuthProvider = ({ children }: PropsWithChildren) => {
  const [isLogin, setIsLogin] = useState<boolean>(
    !!localStorage.getItem("accessToken")
  );

  return (
    <AuthContext.Provider
      value={{
        isLogin,
        setIsLogin,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};
