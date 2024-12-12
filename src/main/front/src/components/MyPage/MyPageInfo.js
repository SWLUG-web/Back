import React, { useEffect, useState, useTransition } from "react";
import { useNavigate } from "react-router-dom";
import { useSelector } from 'react-redux'; 
import { getUserInfo } from '../../services/api';
import "../../styles/input.css";
import "../../styles/common.css";
import "../../styles/MyPageInfo.css";

function MyPageInfo() {
  const user = useSelector(state => state.auth.user);
  const [userInfo, setUserInfo] = useState({
    id: "",
    nickname: "",
    email: "",
    phone: ""
  });

  const navigate = useNavigate();
  const [ _, startTransition] = useTransition();

  const handlePasswordReset = () => {
    startTransition(() => {
      navigate("/users/update", { state: { fromMyPage: true } });
    });
  };

  useEffect(() => {
    const fetchUserInfo = async () => {
      try {
        const response = await getUserInfo({
          id: user.id,  // Redux 상태에서 가져온 사용자 ID
          pw: user.pw   // Redux 상태에서 가져온 비밀번호
        });
        
        if (response.status === 200) {
          setUserInfo(response.data);
        }

      } catch (error) {
        console.error('회원 정보 불러오기 실패:', error);
      }
    };

    if (user) {
      fetchUserInfo();
    }
  }, [user]);

  return (
    <div className="info-form">
      <h1 className="form_subtitle">회원 정보</h1>
      <h4 className="info-text">회원정보 수정 및 탈퇴는 운영진에게 문의해주세요</h4>
      <div className="info-form_field">
        <div className="input-wrapper">
          <label>아이디</label>
          <input value={userInfo.id} className="info-form_input" readOnly  />
        </div>
      </div>
      <div className="info-form_field">
        <div className="input-wrapper">
          <label>닉네임</label>
          <input value={userInfo.nickname} className="info-form_input" readOnly />
        </div>
      </div>

      {/* 비밀번호 재설정 버튼 */}
      <div className="info-form_field">
        <div className="input-wrapper">
          <label>비밀번호</label>
            <button
              onClick={handlePasswordReset}
              className="password-reset-button"
            >
              비밀번호 재설정
            </button>
          </div>
      </div>

      <div className="info-form_field">
        <div className="input-wrapper">
          <label>이메일</label>
          <input value={userInfo.email} className="info-form_input" readOnly />
        </div>
      </div>
      <div className="info-form_field">
        <div className="input-wrapper">
          <label>전화 번호</label>
          <input value={userInfo.phone} className="info-form_input" readOnly />
        </div>
      </div>
    </div>
  );
}

export default MyPageInfo;
