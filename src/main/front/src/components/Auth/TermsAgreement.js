import React, { useState, useEffect } from 'react';
import "../../styles/TermsAgreement.css"; // 공통 스타일 CSS 파일을 적용
import "../../styles/common.css";
import PrevNextButtons from "../../components/Auth/PrevNextButtons";

function TermsAgreement({ onNext, onPrev }) {
  const [allCheck, setAllCheck] = useState(false);
  const [firstCheck, setFirstCheck] = useState(false);
  const [secondCheck, setSecondCheck] = useState(false);
  const [thirdCheck, setThirdCheck] = useState(false);

  // 전체 동의 체크박스 이벤트
  const allBtnEvent = () => {
    const newCheckStatus = !allCheck;
    setAllCheck(newCheckStatus);
    setFirstCheck(newCheckStatus);
    setSecondCheck(newCheckStatus);
    setThirdCheck(newCheckStatus);
  };

  // 개별 체크박스 이벤트
  const firstBtnEvent = () => {
    setFirstCheck(!firstCheck);
  };

  const secondBtnEvent = () => {
    setSecondCheck(!secondCheck);
  };

  const thirdBtnEvent = () => {
    setThirdCheck(!thirdCheck);
  };

  // 모든 필수 체크박스가 체크되었는지 확인하여 전체 체크박스 상태 업데이트
  useEffect(() => {
    if (firstCheck && secondCheck) {
      setAllCheck(true);
    } else {
      setAllCheck(false);
    }
  }, [firstCheck, secondCheck]);

  return (
    <form method="post" className="form">
      <h1 className="form_title">회원가입</h1>

      <div className="form_steps">
        <span className="form_step current">1. 개인정보 수집 이용 약관 동의</span>
        <span className="form_step">2. 정보 입력</span>
        <span className="form_step">3. 회원 가입 신청 완료</span>
      </div>
      
      <div className="form_agreement">
        <div className="form_agreement_box">
          <div className="form_agreement_all">
            <input type="checkbox" id="all-check" checked={allCheck} onChange={allBtnEvent} />
            <label htmlFor="all-check">전체동의</label>
          </div>

          <div className="form_agreement_item">
            <div className="form_agreement_label">
              <input type="checkbox" id="check1" checked={firstCheck} onChange={firstBtnEvent} />
              <label htmlFor="check1">
                <span className="required">[필수]</span> 1~~~ 
              </label>
            </div>
            <div className="agreement_text">
              회원으로 가입하시면 네이버 서비스를 보다 편리하게 이용할 수 있습니다.
              여러분은 본 약관을 읽고 동의하신 후 회원 가입을 신청하실 수 있으며, 네이버는 이에 대한 승낙을 통해 회원 가입 절차를 완료하고 여러분께 네이버 서비스 이용 계정(이하 ‘계정’)을 부여합니다. 계정이란 회원이 네이버 서비스에 로그인한 이후 이용하는 각종 서비스 이용 이력을 회원 별로 관리하기 위해 설정한 회원 식별 단위를 말합니다.
              회원은 자신의 계정을 통해 좀더 다양한 네이버 서비스를 보다 편리하게 이용할 수 있습니다. 이와 관련한 상세한 내용은 계정 운영정책 및 고객센터 내 네이버 회원가입 방법 등에서 확인해 주세요.
              네이버는 단체에 속한 여러 구성원들이 공동의 계정으로 네이버 서비스를 함께 이용할 수 있도록 단체회원 계정도 부여하고 있습니다. 단체회원 구성원들은 하나의 계정 및 아이디(ID)를 공유하되 각자 개별적으로 설정한 비밀번호를 입력하여 계정에 로그인하고 각종 서비스를 이용하게 됩니다. 단체회원은 관리자와 멤버로 구성되며, 관리자는 구성원 전부로부터 권한을 위임 받아 단체회원을 대표하고 단체회원 계정을 운용합니다. 따라서 관리자는 단체회원 계정을 통해 별도 약관 또는 기존 약관 개정에 대해 동의하거나 단체회원에서 탈퇴할 수 있고, 멤버들의 단체회원 계정 로그인 방법 및 이를 통한 게시물 게재 등 네이버 서비스 이용을 관리(게시물 삭제 포함)할 수 있습니다. 본 약관에서 규정한 사항은 원칙적으로 구성원 모두에게 적용되며, 각각의 구성원은 다른 구성원들의 단체회원 계정 및 아이디(ID)를 통한 서비스 이용에 관해 연대책임을 부담합니다.
              단체회원 계정 사용에서의 관리자, 멤버 등의 권한 및 (공동)책임에 관한 사항 등은 계정 운영정책 및 고객센터 내 네이버 단체회원(단체 아이디) 소개 등에서 확인해 주시기 바랍니다.</div>
          </div>

          <div className="form_agreement_item">
            <div className="form_agreement_label">
              <input type="checkbox" id="check2" checked={secondCheck} onChange={secondBtnEvent} />
              <label htmlFor="check2">
                <span className="required">[필수]</span> 2~~~
              </label>
            </div>
            <div className="agreement_text">필수 동의 관련 내용 2</div>
          </div>

          <div className="form_agreement_item">
            <div className="form_agreement_label">
              <input type="checkbox" id="check3" checked={thirdCheck} onChange={thirdBtnEvent} />
              <label htmlFor="check3">[선택] 3~~~</label>
            </div>
            <div className="agreement_text">선택 동의 관련 내용</div>
          </div>
        </div>
      </div>

      <PrevNextButtons
        onPrev={onPrev}
        onNext={() => {
          if (firstCheck && secondCheck) {
            onNext();
          } else {
            alert("모든 필수 항목에 동의해 주세요.");
          }
        }}
      />
    </form>
  );
}

export default TermsAgreement;
