import React from 'react';
import "../styles/common.css";
import "../styles/NotFound.css";

function NotFound() {
  return (
    <div
      className="error-page container mx-auto px-4 py-8 flex flex-col items-center justify-center h-screen"
      style={{
        backgroundImage: 'url(/apply_back.png)',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
      }}
    >
      {/* 가로 정렬을 위한 Flex Container */}
      <div className="error-header flex items-center mb-6">
        <img
          src="/Logo5.png"
          alt="SWLUG Logo"
          className="error-logo"
        />
        <h1
          className="error-title text-4xl font-bold"
          style={{ fontSize: '36px', color: '#202123', marginRight: '8px'}}
        >
          오류 
        </h1>
        <h1
          className="error-title text-4xl font-bold"
          style={{ fontSize: '30px', color: '#6A6A6A' }}
        >
          Error
        </h1>
      </div>

      <p className="error-description-kr text-lg leading-6 text-center mb-6" style={{ color: '#202123' }}>
        페이지를 처리 중에 오류가 발생했습니다. 서비스 이용에 불편을 드려 죄송합니다.<br />
        요청하신 정보가 정확한지 확인 후 다시 시도해 주시기 바랍니다. <br />
      </p>
      <p className="error-description text-lg leading-6 text-center mb-6" style={{ color: '#6A6A6A' }}>
        We have encountered a system while processing your request. <br />
        We apologize for the inconvenience. Please check your request and try again.
      </p>
      <button
        className="error-btn px-6 py-3 rounded-lg"
        style={{ backgroundColor: '#202123', color: '#F9F9F9', fontSize: '20px', borderRadius: '30px', }}
        onClick={() => window.location.href = '/'}
      >
        Main →
      </button>
    </div>
  );
}

export default NotFound;
