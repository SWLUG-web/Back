// src/pages/Home/HomePage.js
import React from 'react';
import HomeMain from '../../components/Home/HomeMain';
import RecentNotices from '../../components/Home/RecentNotices'; // RecentNotices 추가

const HomePage = () => {
  return (
    <div className="container mx-auto px-4 py-8 bg-white">
      <HomeMain />
      {/* 공지사항 추가 */}
      <div className="mt-12">
        <RecentNotices />
      </div>
    </div>
  );
};

export default HomePage;
