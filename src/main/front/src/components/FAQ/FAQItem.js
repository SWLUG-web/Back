import React, { useState } from 'react';
import '../../styles/FAQPage.css';

const FAQItem = ({ question, answer }) => {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <div className="faq-item">
      <div
        className="faq-question"
        onClick={() => setIsOpen((prev) => !prev)}
      >
        <span>{question}</span>
        <img
          src={isOpen ? '/faq_up.png' : '/faq_down.png'}
          alt="Toggle"
          className="faq-toggle-icon"
        />
      </div>
      {isOpen && (
        <div className="faq-answer">
          {/* 펭귄 이미지 */}
          <img
            src="/faq_penguin.png"
            alt="Penguin"
            className="faq-penguin"
            style={{ width: '205px', height: '179px' }} // 펭귄 크기
          />
          {/* 말풍선 */}
          <div
            className="faq-speech-bubble"
            style={{
              width: '454px',
              height: '195px',
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              padding: '20px',
              borderRadius: '10px',
              backgroundColor: '#f1f1f1',
              boxShadow: '0 4px 10px rgba(0, 0, 0, 0.1)',
            }}
          >
            {answer}
          </div>
        </div>
      )}
    </div>
  );
};

export default FAQItem;