import {
  createBrowserRouter,
  Navigate,
  RouterProvider,
} from "react-router-dom";
import RootLayout from "./Layout";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import LoginPage from "./pages/LoginPage";
import TodayPage from "./pages/TodayPage";
import YesterdayPage from "./pages/YesterdayPage";
import TomorrowPage from "./pages/TomorrowPage";
import SignupPage from "./pages/SignupPage";
import { useAuthContext } from "./context/AuthContext";
import { AuthProvider } from "./context/AuthProvier";
import type { PropsWithChildren } from "react";

const queryClient = new QueryClient();

const AuthRoute = ({ children }: PropsWithChildren) => {
  const { isLogin } = useAuthContext();
  if (isLogin == false) {
    alert("로그인을 해주세요.");
    return <Navigate to="/" replace />;
  }
  return children;
};

const router = createBrowserRouter([
  {
    path: "/",
    element: <RootLayout />,
    errorElement: <></>,
    children: [
      {
        index: true,
        element: <LoginPage />,
      },
      {
        path: "/signup",
        element: <SignupPage />,
      },
      {
        path: "/today",
        element: (
          <AuthRoute>
            <TodayPage />
          </AuthRoute>
        ),
      },
      {
        path: "/yesterday",
        element: (
          <AuthRoute>
            <YesterdayPage />
          </AuthRoute>
        ),
      },
      {
        path: "/tomorrow",
        element: (
          <AuthRoute>
            <TomorrowPage />
          </AuthRoute>
        ),
      },
    ],
  },
]);

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <RouterProvider router={router} />
      </AuthProvider>
    </QueryClientProvider>
  );
}

export default App;
