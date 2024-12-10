import { useState, useEffect } from 'react';
import axios from 'axios'; // 실제 API 호출에 사용

const useAuth = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    // 로그인 상태를 확인하고 `isAuthenticated` 상태를 업데이트합니다.
    const token = localStorage.getItem('authToken');
    setIsAuthenticated(token !== null);
  }, []);

  return isAuthenticated;
};

// Mock 활성화 여부
const isMock = true; // true일 경우 Mock 데이터를 사용, false면 실제 API 호출

// Mock API: 비밀번호 재설정
const mockResetPassword = async (formData) => {
  const mockData = {
    status: 200,
    message: '비밀번호가 성공적으로 재설정되었습니다.',
  };

  return new Promise((resolve) =>
    setTimeout(() => resolve(mockData), 1000) // 1초 대기 후 응답 반환
  );
};

// 비밀번호 재설정 - 로그인
export const resetPassword = async (formData) => {
  const { id, newPassword } = formData;

  if (isMock) {
    // Mock API 호출
    return mockResetPassword(formData);
  }

  // 실제 API 호출
  const response = await axios.post('/users/update', {
    id,
    pw: newPassword,
    pwdCheck: true, // 비밀번호 재설정 요청임을 명시
  });

  return response.data;
};

// 비밀번호 변경 - 마이페이지
export const changePassword = async (formData) => {
  const { currentPassword, newPassword } = formData;

  if (isMock) {
    // Mock API 호출
    return mockChangePassword(formData);
  }

  // 실제 API 호출
  const response = await axios.post('/users/update', {
    currentPw: currentPassword,
    pw: newPassword,
    pwdCheck: true, // 비밀번호 변경 요청임을 명시
  });

  return response.data;
};

export default useAuth;
