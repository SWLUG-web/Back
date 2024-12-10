import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "../../styles/common.css";
import "../../styles/RegistrationComplete.css"

const RegistrationComplete = ({ formData }) => {
  const navigate = useNavigate();

  useEffect(() => {
    if (!formData.roleType) {
      navigate('/users/login');
    }
  }, [formData, navigate]);

  const handleHomeClick = () => {
    navigate("/main");
  };

  return (
    <div className="form">
      <h1 className="form_title">회원가입</h1>

      <div className="form_steps">
        <span className="form_step">1. 개인정보 수집 이용 약관 동의</span>
        <span className="form_step">2. 정보 입력</span>
        <span className="form_step current">3. 회원 가입 신청 완료</span>
      </div>

      <div className="join_com">
        <img src="/img/Logo4.png" alt="SWLUG Logo" className="join_logo" />
        <p className="join_message">
          회원 가입 신청이 완료되었습니다.
          <br />
          최종 회원 가입을 위해서는 가입 신청 후 별도의 승인처리가 진행되며,
          <br />
          승인완료까지 1~2일이 소요될 수 있습니다.
        </p>
        <button type="button" className="btn_home" onClick={handleHomeClick}>
          홈으로
        </button>
      </div>
    </div>
  );
};

export default RegistrationComplete;