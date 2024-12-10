import React, { useState } from "react";
import { register } from '../../services/api';
import PrevNextButtons from "../../components/Auth/PrevNextButtons";
import "../../styles/UserRegistration.css"; 
import "../../styles/input.css"
// import axios from 'axios'; // 백엔드 연동 시 주석 해제

const UserRegistration = ({ onNext, onPrev }) => {
  const [formData, setFormData] = useState({
    id: "",
    pw: "",
    pwCheck: "",
    name: "",
    email: "",
    emailAuth: "",
    phone: "",
    privacyCheck: true
  });

  const [formErrors, setFormErrors] = useState({
    idError: false,
    pwMatchError: false,
    emailAuthError: false,
  });

  const [authButtonText, setAuthButtonText] = useState("인증번호 전송"); // 버튼 텍스트 상태

  const [error, setError] = useState("");
  const [passwordMatch, setPasswordMatch] = useState(false);

  const [authSuccess, setAuthSuccess] = useState(false); // 이메일 인증 성공 상태

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });

    // 비밀번호 확인 로직
    if (name === "pw" || name === "pwCheck") {
      const match = name === "pw" ? value === formData.pwCheck : value === formData.pw;
      setPasswordMatch(match);
      setError(match ? "" : "비밀번호가 일치하지 않습니다.");
    }

    // 인증 번호 확인 로직
    if (name === "emailAuth") {
      const isAuthSuccess = value === "123456";
      setAuthSuccess(isAuthSuccess);
      setFormErrors(prev => ({
        ...prev,
        emailAuthError: !isAuthSuccess && value !== ''
      }));
    }
  };

  const handleAuthButtonClick = () => {
    // 인증번호 전송 로직 (백엔드 연동 필요)
    //alert("인증번호가 전송되었습니다.");
    setAuthButtonText("인증번호 재전송"); // 버튼 텍스트 변경
  };

  const handleSubmit = async () => {
    let errors = {
      idError: !formData.id.match(/^\d{10}$/),
      pwMatchError: formData.pw !== formData.pwCheck,
      emailAuthError: !authSuccess,
    };
    
    setFormErrors(errors);
  
    if (!errors.idError && !errors.pwMatchError && !errors.emailAuthError) {
      try {
        const submitData = {
          id: formData.id,
          pw: formData.pw,
          pwCheck: formData.pw === formData.pwCheck,
          name: formData.name,
          email: formData.email,
          emailAuth: authSuccess,
          phone: formData.phone,
          privacyCheck: true
        };
        
        const response = await register(submitData);
        if (response.status === 200) {
          onNext({
            ...formData,
            roleType: response.data.roleType
          });
        }
      } catch (error) {
        console.error('Registration error:', error);
        alert('회원가입 중 오류가 발생했습니다.');
      }
    }
  };

  return (
    <form method="post" className="form">
      <h1 className="form_title">회원가입</h1>

      <div className="form_steps">
        <span className="form_step">1. 개인정보 수집 이용 약관 동의</span>
        <span className="form_step current">2. 정보 입력</span>
        <span className="form_step">3. 회원 가입 신청 완료</span>
      </div>

      <div className="info-form">
        <h1 className="info-text">입력한 정보를 기반으로 회원가입 요청이 되므로, 정확한 정보를 기입해주세요.</h1>

        <div className="info-form_field">
          <div className="input-wrapper">
            <label htmlFor="id">아이디</label>
            <input
              name="id"
              id="id"
              placeholder="아이디(학번)을 입력해주세요"
              value={formData.id}
              onChange={handleChange}
              className="info-form_input"
            />
          </div>
          {formErrors.idError && (
            <span className="info-form_error">형식이 맞지 않습니다.</span>
          )}
        </div>

        <div className="info-form_field">
          <div className="input-wrapper">
            <label htmlFor="pw">비밀번호</label>
            <input
              name="pw"
              id="pw"
              type="password"
              placeholder="비밀번호를 입력해주세요"
              value={formData.pw}
              onChange={handleChange}
              className="info-form_input"
            />
          </div>
        </div>
        <div className="info-form_field">
              <div className="input-wrapper">
                <label htmlFor="pwCheck">비밀번호 확인</label>
                    <div className="input-container">
                    <input
                      name="pwCheck"
                      id="pwCheck"
                      type="password"
                      value={formData.pwCheck}
                      onChange={handleChange}
                      className="info-form_input"
                    />
                        {passwordMatch && (
                            <img 
                                src="/pwCheck.png" 
                                alt="check" 
                                className="check-icon"
                            />
                        )}
                    </div>
                </div>
                {error && <div className="info-form_error">{error}</div>}
            </div>

        <div className="info-form_field">
          <div className="input-wrapper">
            <label htmlFor="name">닉네임</label>
            <input
              name="name"
              id="name"
              placeholder="[예시]26기_김민지"
              value={formData.name}
              onChange={handleChange}
              className="info-form_input"
            />
          </div>
        </div>

        <h1 className="info-text">본인확인을 위해 [슈러그] 네이버 카페에 가입한 이메일을 기입해주세요.</h1>

        <div className="info-form_field">
          <div className="input-wrapper">
            <label htmlFor="email">이메일</label>
            <div className="input-container">
              <input
                name="email"
                id="email"
                placeholder="swlug@naver.com"
                value={formData.email}
                onChange={handleChange}
                className="info-form_input"
              />
              <button
                type="button"
                className="auth-button"
                onClick={handleAuthButtonClick} // 버튼 클릭 핸들러 추가
              >
                {authButtonText} {/* 버튼 텍스트 상태 */}
              </button>
            </div>
          </div>
        </div>

        <div className="info-form_field">
          <div className="input-wrapper">
            <label htmlFor="emailAuth">인증 번호</label>
            <div className="input-container">
              <input
                name="emailAuth"
                id="emailAuth"
                placeholder="인증번호를 입력하세요"
                value={formData.emailAuth}
                onChange={handleChange}
                className="info-form_input"
              />
              {/* 인증 성공 상태일 때 아이콘 표시 */}
              {authSuccess && (
                <img 
                  src="/pwCheck.png" 
                  alt="check" 
                  className="check-icon"
                />
              )}
            </div>
          </div>
          {formErrors.emailAuthError && (
            <span className="info-form_error">인증에 실패했습니다.</span>
          )}
        </div>
        <h1 className="info-text">전화번호는 공백없이 -로 구분하여 입력해주세요.</h1>

        <div className="info-form_field">
          <div className="input-wrapper">
            <label htmlFor="phone">전화 번호</label>
            <input
              name="phone"
              id="phone"
              placeholder="010-1234-1234"
              value={formData.phone}
              onChange={handleChange}
              className="info-form_input"
            />
          </div>
        </div>
      </div>

      <PrevNextButtons onPrev={onPrev} onNext={handleSubmit} />
    </form>
  );
};

export default UserRegistration;