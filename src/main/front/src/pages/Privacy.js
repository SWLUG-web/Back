import React from 'react';
import "../styles/common.css";
import "../styles/Privacy.css";

function Privacy() {
  return (
    <div className="privacy-container text-left">
      <p style={{ fontWeight: "900", fontSize: "40px" }}>SWLUG</p>
      <h1 className="privacy-title">개인정보처리방침</h1>
      <hr style={{ border: "0", height: "1px", backgroundColor: "#202123" }}/>
      <br/>
      <p className="privacy-intro">
        [SWLUG] (이하 ‘소학회’)는 사용자의 개인정보를 중요시하며, 이를 보호하기 위해 최선을 다하고 있습니다. 
        본 방침은 소학회 웹사이트를 통해 수집된 개인정보가 어떻게 사용되고 보호되는지를 명확히 설명하며, 사용자 권리와 선택 사항에 대해 안내합니다. 
      </p>

      <section>
        <h2 className="privacy-heading">1. 개인정보 수집 항목 및 방법</h2>
        <p>(1) 수집 항목<br/>
          소학회는 회원가입 및 서비스 제공 과정에서 아래의 개인정보를 수집합니다.<br/><br/>
        </p>
        <ul>
          <li>- 회원가입 시
            <ul>
              <li>- 필수항목: 이름, 이메일, 전화번호, 비밀번호, 학번(ID)</li>
            </ul>
          </li>
          <li>- 소학회 활동 지원 시
            <ul>
              <li>- 필수항목: 이름, 학과, 학번, 연락처</li>
              <li>- 선택정보: 활동 경력, 자격증, 수상 이력</li>
            </ul>
          </li>
          <li>- 자동 수집 항목
            <ul>
              <li>- 쿠키, 접속 IP 정보, 서비스 이용 기록</li><br/>
            </ul>
          </li>
        </ul>
        <p>(2) 수집 방법</p>
        <ul>
          <li>- 회원가입, 서비스 이용 시 사용자가 직접 입력</li>
          <li>- 소학회 활동 신청을 위해 구글폼을 통한 수집</li>
          <li>- 자동 수집 도구를 통한 정보 수집</li>
        </ul>
      </section>

      <section>
        <h2 className="privacy-heading">2. 개인정보의 수집 및 이용 목적</h2>
        <p>
          소학회는 수집한 개인정보를 다은과 같은 목적으로 사용합니다.<br/><br/>
        </p>
        <ul>
          <li>- 회원 관리: 회원 식별, 공지사항 전달, 문의사항 응대</li>
          <li>- 소학회 활동 지원: 소학회 지원자의 적합성 평가 및 연락</li>
        </ul>
      </section>

      <section>
        <h2 className="privacy-heading">3. 개인정보의 보유 및 이용 기간</h2>
        <ul>
          <li>- 회원 탈퇴 시 또는 동아리 활동 종료 후 6개월간 보관 후 삭제</li>
          <li>- 소학회 활동 신청 기간 후 즉시 파기</li>
          <li>- 관계 법령에 따른 보관이 필요한 경우 해당 법령에서 정한 기간 동안 보관
            <ul>
              <li>- 예: 통신비밀보호법에 따른 접속 기록 3개월 보관</li>
            </ul>
          </li>
        </ul>
      </section>

      <section>
        <h2 className="privacy-heading">4. 개인정보의 제3자 제공</h2>
        <p>
          소학회는 사용자의 개인정보를 원칙적으로 제3자에게 제공하지 않습니다.<br/>
          단, 다음의 경우는 예외로 합니다.<br/><br/>
        </p>
        <ul>
            <li>- 법적 의무 준수를 위해 필요한 경우</li>
            <li>- 사용자의 사전 동의를 받은 경우</li>
        </ul>
      </section>

      <section>
        <h2 className="privacy-heading">5. 개인정보의 파기</h2>
        <p>
        소학회는 개인정보 보유 기간이 경과하거나 처리 목적이 달성되면 해당 정보를 지체 없이 파기합니다.<br/><br/>
        </p>
        <ul>
          <li>- 전자 파일: 복구 불가능한 방법으로 삭제</li>
          <li>- 종이 문서: 분쇄 또는 소각</li>
        </ul>
      </section>

      <section>
        <h2 className="privacy-heading">6. 사용자 권리 및 행사 방법</h2>
        <ul>
          <li>사용자는 다음과 같은 권리를 행사할 수 있습니다.<br/><br/>
            <ul>
              <li>- 개인정보 조회, 수정, 삭제 요청</li>
              <li>- 회원 탈퇴 및 개인정보 처리 정지 요청</li><br/>
            </ul>
          </li>
          <p>
          권리 행사는 이메일 또는 문의하기 페이지를 통해 요청하실 수 있습니다.
          </p>
        </ul>
      </section>

      <section>
        <h2 className="privacy-heading">7. 개인정보 보호책임자</h2>
        <ul>
          <li>사용자의 개인정보를 보호하고 관련 불만을 처리하기 위해 다음과 같이 개인정보 보호책임자를 지정하고 있습니다.<br/><br/>
            <ul>
              <li>- 책임자: [책임자 이름]</li>
              <li>- 이메일: swu.swlug@gmail.com</li>
              <li>- 전화번호: [책임자 연락처]</li>
            </ul>
          </li>
        </ul>
      </section>

      <section>
        <h2 className="privacy-heading">8. 기타 안내</h2>
        <ul>
          <li>개인정보 침해로 인한 신고나 상담이 필요할 경우 아래 기관에 문의할 수 있습니다.<br/><br/>
            <ul>
              <li>- 개인정보침해신고센터: [privacy.kisa.or.kr](http://privacy.kisa.or.kr/) / 118</li>
              <li>- 경찰청 사이버안전국: [cyberbureau.police.go.kr](http://cyberbureau.police.go.kr/) / 182</li><br/>
            </ul>
          </li>
          <p>
            본 방침은 [사이트 배포일]부터 적용됩니다.
          </p>
        </ul>
      </section>
    </div>
  );
}

export default Privacy;
