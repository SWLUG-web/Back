// 로그인 비밀번호 재설정

import React from "react";
import PasswordReset from "../../components/Auth/ResetPassword";

function ResetPasswordPage() {
  return (
    <div>
      <PasswordReset isLoginPage={true} />
    </div>
  );
}

export default ResetPasswordPage;