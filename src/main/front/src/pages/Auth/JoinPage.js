import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import TermsAgreement from "../../components/Auth/TermsAgreement"; // 약관 동의 컴포넌트
import UserRegistration from "../../components/Auth/UserRegistration"; // 회원가입 컴포넌트
import RegistrationComplete from "../../components/Auth/RegistrationComplete"; // 완료 화면 컴포넌트

const JoinPage = () => {
  const [step, setStep] = useState(1);
  const [formData, setFormData] = useState({});
  const navigate = useNavigate();

  const handleNext = (data) => {
    setFormData({ ...formData, ...data });
    setStep(step + 1);
  };

  const handlePrev = () => {
    if (step > 1) {
      setStep(step - 1);
    } else {
      // 첫 번째 단계에서 이전 버튼을 누르면 로그인 페이지로 이동
      navigate("/users/login");
    }
  };

  return (
    <div>
      {step === 1 && <TermsAgreement onNext={handleNext} onPrev={handlePrev} />}
      {step === 2 && <UserRegistration onNext={handleNext} onPrev={handlePrev} />}
      {step === 3 && <RegistrationComplete formData={formData} />}
    </div>
  );
};

export default JoinPage;
