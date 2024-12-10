import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import '../../styles/Home.css'; // 스타일 추가
import ReactPlayer from "react-player";

const HomeMain = () => {
  const [hoverIndex, setHoverIndex] = useState(null); // hover 상태 관리

  // 주요 활동 데이터
  const activities = [
    { id: 1, title: '스터디 📚', image: '/hover1.jpg', link: '/intro' },
    { id: 2, title: '프로젝트 👩‍💻', image: '/hover2.png', link: '/intro' },
    { id: 3, title: '특강 / 멘토-멘티 👩‍🏫', image: '/hover3.png', link: '/intro' },
    { id: 4, title: '세미나 🔐', image: '/hover4.png', link: '/intro' },
  ];

  return (
    <div>
      {/* 메인 콘텐츠 */}
      <div className="container mx-auto px-4 py-12 flex flex-col lg:flex-row items-center">
        {/* Left: YouTube Video */}
        <div className="w-full lg:w-1/2 mb-8 lg:mb-0 flex justify-center">
          <div className="relative" style={{ width: '80%', maxWidth: '600px' }}>
            {/*<iframe*/}
            {/*  width="100%"*/}
            {/*  height="315"*/}
            {/*  src="https://www.youtube.com/embed/ANyFScpNOW8"*/}
            {/*  title="YouTube video player"*/}
            {/*  frameBorder="0"*/}
            {/*  allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"*/}
            {/*  allowFullScreen*/}
            {/*  className="rounded shadow-md"*/}
            {/*></iframe>*/}
            <ReactPlayer
                className="swlugPlayer"
                url={"https://youtu.be/ANyFScpNOW8?si=ADg-wK7WzuC3QXQs"}
                width='100%'
                height='315px'
                playing={true}
                muted={true}
                controls={true}
                />
          </div>
        </div>
        {/* Right: Text */}
        <div className="w-full lg:w-1/2 text-left lg:pl-8">
          <h1 className="text-4xl font-bold mb-4">SWLUG</h1>
          <p className="text-lg mb-4 font-semibold">
            서울여자대학교 정보보호학과 소속 소학회
          </p>
          <p className="text-lg mb-4 whitespace-pre-line">
            서울여자대학교 정보보호학과 소학회 SWLUG는 박후린 교수님의<br />
            지도하에 학부생이 중심이 되어 운영하는 학술 동아리로,<br />
            1999년 대학연합리눅스 유저그룹에 포함된 연합동아리로 시작하였습니다.
          </p>
          <p className="text-lg mb-4 whitespace-pre-line">
            현재 대학정보보호 동아리 KUCIS에 포함되어 있으며
            세미나, 프로젝트, <br /> 스터디, 특강 등 다양한 활동을 이어나가고 있습니다.
          </p>
        </div>
      </div>

      {/* 주요 활동 */}
      <div className="container mx-auto px-4 py-12 text-center">
      <h2
    className="font-bold flex items-center justify-center mb-12"
    style={{ fontSize: '20px' }} // 주요 활동 글자 크기 20px
  >
    주요 활동 <span className="ml-2">🔎</span>
  </h2>
        <div
          className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-2 gap-8 max-w-7xl mx-auto"
          // max-w-5xl을 max-w-7xl로 변경하여 전체 컨테이너 너비를 넓힘
        >
          {activities.map((activity, index) => (
            <Link
              key={activity.id}
              to={activity.link}
              className={`activity-card relative block overflow-hidden rounded-lg shadow-lg bg-white ${
                hoverIndex === index ? 'hover-active' : ''
              }`}
              onMouseEnter={() => setHoverIndex(index)} // Hover 상태 설정
              onMouseLeave={() => setHoverIndex(null)} // Hover 상태 해제
            >
              {hoverIndex === index ? (
                <img
                  src={activity.image}
                  alt={activity.title}
                  className="w-full h-full object-cover rounded-lg"
                />
              ) : (
                <div className="flex items-center justify-center h-full">
                  <span className="text-2xl font-bold">{activity.title}</span>
                </div>
              )}
            </Link>
          ))}
        </div>
      </div>
    </div>
  );
};

export default HomeMain;
