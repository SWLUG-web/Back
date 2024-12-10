import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

// 로그인 관련 API
export const login = async (credentials) => {
  if (process.env.NODE_ENV === 'development') {
    try {
      const response = await fetch('/mock-login.json');
      if (!response.ok) throw new Error('네트워크 응답이 실패했습니다.');
      const data = await response.json();
      
      if (credentials.try >= 5) {
        return { 
          status: 300, 
          message: '비밀번호 입력 횟수를 초과했습니다. 관리자에게 문의하세요.' 
        };
      }

      const user = data.users.find(
        (user) => user.id === credentials.id && user.pw === credentials.pw
      );

      if (user) {
        // 로그인 성공 시 토큰 저장
        localStorage.setItem('authToken', 'mock-token');
        localStorage.setItem('user', JSON.stringify(user));
        return { status: 200, message: 'Login successful' };
      }
      return { 
        status: 401, 
        message: 'Invalid credentials',
        try: credentials.try + 1
      };
    } catch (error) {
      console.error('JSON 파일을 읽는 중 오류 발생:', error);
      throw error;
    }
  }
  
  return axios.post(`${API_URL}/users/login`, credentials);
};

export const logout = async () => {
  if (process.env.NODE_ENV === 'development') {
    return new Promise((resolve) => {
      localStorage.removeItem('authToken');
      localStorage.removeItem('user');
      localStorage.removeItem('userId');
      resolve({ status: 200, message: 'Logout successful' });
    });
  }
  
  const response = await axios.get(`${API_URL}/users/logout`);
  if (response.status === 200) {
    localStorage.removeItem('authToken');
    localStorage.removeItem('user');
    localStorage.removeItem('userId');
  }
  return response;
};

// 회원가입 관련 API
export const register = async (formData) => {
  if (process.env.NODE_ENV === 'development') {
    try {
      const response = await fetch('/mock-join.json');
      if (!response.ok) throw new Error('네트워크 응답이 실패했습니다.');
      
      return {
        status: 200,
        data: {
          roleType: "GUEST"
        }
      };
    } catch (error) {
      console.error('JSON 파일을 읽는 중 오류 발생:', error);
      throw error;
    }
  }
  
  const requestBody = {
    id: formData.id,
    pw: formData.pw,
    pwCheck: formData.pwCheck,
    name: formData.name,
    email: formData.email,
    emailAuth: formData.emailAuth,
    phone: formData.phone,
    privacyCheck: formData.privacyCheck
  };
  
  return axios.post(`${API_URL}/users/join`, requestBody);
};

// 회원정보 관련 API
export const deleteUser = async (userData) => {
  return axios.post(`${API_URL}/users/delete`, userData);
};

// 회원정보 수정 관련 API
export const updateUser = async (userData) => {
  if (process.env.NODE_ENV === 'development') {
    try {
      const response = await fetch('/mock-update-user.json');
      if (!response.ok) throw new Error('네트워크 응답이 실패했습니다.');
      
      return {
        status: 200,
        message: '회원정보가 성공적으로 수정되었습니다.'
      };
    } catch (error) {
      console.error('JSON 파일을 읽는 중 오류 발생:', error);
      throw error;
    }
  }
  
  const requestBody = {
    id: userData.id,
    pw: userData.pw,
    pwdCheck: userData.pwdCheck
  };
  
  return axios.put(`${API_URL}/users/update`, requestBody);
};

// 마이페이지 관련 API
export const getUserInfo = async (userData) => {
  if (process.env.NODE_ENV === 'development') {
    try {
      const response = await fetch('/mock-mypage.json');
      if (!response.ok) throw new Error('네트워크 응답이 실패했습니다.');
      const data = await response.json();
      return {
        status: 200,
        data
      };
    } catch (error) {
      console.error('JSON 파일을 읽는 중 오류 발생:', error);
      throw error;
    }
  }
  
  return axios.post(`${API_URL}/users/mypage`, {
    id: userData.id,
    pw: userData.pw
  });
};

// 게시판 관련 API
export const deletePost = async (page, postData) => {
  return axios.delete(`${API_URL}/${page}/board/delete`, { data: postData });
};

export const updatePost = async (page, postData) => {
  return axios.put(`${API_URL}/${page}/board/write`, postData);
};

export const sendAuthCode = async (email) => {
  if (process.env.NODE_ENV === 'development') {
    // 개발용 mock 데이터 처리
    return new Promise(resolve => 
      setTimeout(() => resolve({ status: 200, message: 'Auth code sent' }), 1000)
    );
  }
  return axios.post(`${API_URL}/users/send-auth-code`, { email });
};