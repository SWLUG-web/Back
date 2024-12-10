import React from 'react';

const Footer = () => {

  const handlePrivacyClick = () => {
    window.location.href = '/privacy';
  };

  return (
    <footer className="footer bg-black text-white py-6">
      <div className="container mx-auto flex flex-col md:flex-row justify-between items-center px-6">
        {/* 왼쪽: Contact Us 및 소셜 아이콘 */}
        <div className="social-and-contact text-left">
          {/* Contact Section */}
          <div className="contact-info">
            <h3 className="text-lg font-semibold mb-2">Contact Us</h3>
            <p className="text-sm">
              00기 학회장_김슈니: <a href="mailto:swny.swu.ac.kr" className="hover:underline">swny.swu.ac.kr</a>
            </p>
            <p className="text-sm">
              00기 부학회장_이슈니: <a href="mailto:swulee.swu.ac.kr" className="hover:underline">swulee.swu.ac.kr</a>
            </p>
          </div>

          {/* Social Icons */}
          <div className="social-icons flex space-x-4 mt-4">
            <img src="/instagram.png" alt="Instagram" className="w-6 h-6" />
            <img src="/kakao.png" alt="Kakao" className="w-6 h-6" />
            <img src="/mail.png" alt="Mail" className="w-6 h-6" />
            <img src="/naver.png" alt="Naver" className="w-6 h-6" />
          </div>
        </div>

        {/* 오른쪽: 개인정보처리방침, 로고, 저작권 */}
        <div className="logo-and-policy text-right flex flex-col items-end">
        <div className="privacy-policy text-sm mb-2" onClick={handlePrivacyClick}>개인정보처리방침</div>
          <img src="/footer_logo.png" alt="SWLUG Footer Logo" className="w-16 h-auto mt-2" 
            onClick={handlePrivacyClick}
          />
          <p className="text-sm mt-2">&copy; 2025 SWLUG. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
