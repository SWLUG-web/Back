// 마이페이지 비밀번호 재설정

import React from "react";
import PasswordReset from "../../components/Auth/ResetPassword";

function ResetPasswordPage() {
  return (
    <div>
      <PasswordReset isLoginPage={false} />
    </div>
  );
}

export default ResetPasswordPage;
