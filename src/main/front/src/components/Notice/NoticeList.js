import React from 'react';

const NoticeList = ({ notices }) => {
  return (
    <div className="notice-list">
      {/* 헤더 추가 */}
      <div className="notice-header flex items-center justify-between py-3 border-b-2 border-black font-bold text-center">
        <div className="flex-shrink-0 w-10">번호</div>
        <div className="flex-grow text-center">제목</div> {/* 가운데 정렬 */}
        <div className="flex-shrink-0 w-32">작성일</div>
        <div className="flex-shrink-0 w-20">작성자</div>
      </div>
      {notices.map((notice, index) => (
        <div
          key={notice.id}
          className="notice-item flex items-center justify-between py-3 border-b border-gray-300"
        >
          <div className="flex-shrink-0 w-10 text-center">{notice.id}</div>
          <div className="flex-grow text-center truncate">{notice.title}</div> {/* 가운데 정렬 */}
          <div className="flex-shrink-0 w-32 text-center">{notice.date}</div>
          <div className="flex-shrink-0 w-20 text-center">{notice.author}</div>
        </div>
      ))}
    </div>
  );
};

export default NoticeList;
