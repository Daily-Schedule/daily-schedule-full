import { createBrowserRouter, RouterProvider } from "react-router-dom";
import RootLayout from "./Layout";
import { HomePage } from "./pages/HomePage";

import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import LoginPage from "./pages/LoginPage";
import TodayPage from "./pages/TodayPage";
import YesterdayPage from "./pages/YesterdayPage";
import TomorrowPage from "./pages/tomorrowPage";
import SignupPage from "./pages/SignupPage";

const queryClient = new QueryClient();

const router = createBrowserRouter([
  {
    path: "/",
    element: <RootLayout />,
    errorElement: <></>,
    children: [
      {
        index: true,
        element: <HomePage />,
      },
      {
        path: "/login",
        element: <LoginPage />,
      },
      {
        path: "/signup",
        element: <SignupPage />,
      },
      {
        path: "/today",
        element: <TodayPage />,
      },
      {
        path: "/yesterday",
        element: <YesterdayPage />,
      },
      {
        path: "/tomorrow",
        element: <TomorrowPage />,
      },
    ],
  },
]);

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <RouterProvider router={router} />
    </QueryClientProvider>
  );
}

export default App;
