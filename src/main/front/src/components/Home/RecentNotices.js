import React from 'react';
import { Link } from 'react-router-dom'; // Link 컴포넌트 추가
import notices from '../../data/notices'; // 공지사항 데이터 import

const RecentNotices = () => {
  // 최신 공지사항 3개 가져오기 (데이터 역순 정렬 후 상위 3개 추출)
  const recentNotices = [...notices].reverse().slice(0, 3);

  return (
    <div>
      {/* 제목 */}
      <h2
  className="font-bold text-center mb-8"
  style={{ fontSize: '20px' }} // 공지사항 글자 크기 20px
>
  공지사항<span className="ml-2">📌</span>
</h2>


      {/* 공지사항 리스트 */}
      <div className="notice-list">
        {/* 헤더 */}
        {/*
        <div className="notice-header flex items-center justify-between py-3 border-b-2 border-black font-bold text-center">
          <div className="flex-shrink-0 w-10">번호</div>
          <div className="flex-grow">제목</div>
          <div className="flex-shrink-0 w-32">작성일</div>
          <div className="flex-shrink-0 w-20">작성자</div>
        </div>
        */}
        
        {/* 공지사항 항목 */}
        {recentNotices.map((notice) => (
          <div
            key={notice.id}
            className="notice-item flex items-center justify-between py-3 border-b border-gray-300"
          >
            <div className="flex-shrink-0 w-10 text-center">{notice.id}</div>
            <div className="flex-grow text-center truncate">{notice.title}</div>
            <div className="flex-shrink-0 w-32 text-center">{notice.date}</div>
            <div className="flex-shrink-0 w-20 text-center">{notice.author}</div>
          </div>
        ))}
      </div>

      {/* More 버튼 */}
      <div className="flex justify-end mt-4">
        <Link
          to="/notice"
          className="flex items-center justify-center text-sm px-4 py-2 border border-gray-500 rounded-full hover:bg-gray-100 transition"
          style={{
            borderRadius: '9999px', // 완전한 둥근 모양
            border: '1px solid #555', // 테두리 색상
            padding: '0.5rem 1.5rem', // 내부 여백
            fontSize: '0.875rem', // 텍스트 크기
            fontWeight: 'bold', // 텍스트 굵기
          }}
        >
          MORE → {/* 화살표 포함 */}
        </Link>
      </div>
    </div>
  );
};

export default RecentNotices;
