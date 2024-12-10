import React from "react";
import "../../styles/MajorActs.css";

const MajorActs = () => {
    const activities = [
        {
            title: "📚 다양한 분야에 대한 스터디",
            description: [
                "리버싱, 시스템 해킹, 웹해킹, 포렌식 등 다양한 분야의 스터디를 운영하고 있어요.",
                "스터디에 필요한 책, 강의 등의 자원은 동아리 자료를 대여해주거나, 운영비를 지원하고 있어요.",
            ],
            image: "/study-image.png", // 이미지 경로
        },
        {
            title: "🔒 동아리원들과 함께하는 프로젝트",
            description: [
                "리버싱, 시스템 해킹, 웹해킹, 포렌식 등 자신이 원하는 분야의 스터디를 팀원들과 모아 운영할 수 있어요!",
                "스터디에 필요한 책, 강의 등의 준비는 동아리 내부 자료로 대여해주거나, 운영비를 지원해줘요.",
            ],
            image: "/project-image.png", // 이미지 경로
        },
        {
            title: "👩‍🏫 선후배와 함께하는 특강과 멘토-멘티 활동",
            description: [
                "선후배 간의 특강과 멘토링 활동을 통해 배움과 경험을 공유하는 기회를 제공합니다.",
                "다양한 주제를 다루며 실무 경험과 학문적 지식을 함께 습득할 수 있어요.",
            ],
            image: "/mentoring-image.png", // 이미지 경로
        },
        {
            title: "🔐 보안 세미나 참여 및 개최",
            description: [
                "국내외 보안 세미나에 참가하며 최신 보안 트렌드를 배우고 경험할 수 있어요.",
                "동아리 내부적으로도 세미나를 개최해 정보를 공유하는 자리를 마련합니다.",
            ],
            image: "/seminar-image.png", // 이미지 경로
        },
    ];

    return (
        <div className="major-acts">
            {activities.map((activity, index) => (
                <div key={index} className="activity-group">
                    <h2 className="activity-title">{activity.title}</h2>
                    <div className="activity-content">
                        <div className="activity-card">
                            <img src={activity.image} alt={activity.title} className="activity-image" />
                            <p>{activity.description[0]}</p>
                        </div>
                        <div className="activity-card">
                            <img src={activity.image} alt={activity.title} className="activity-image" />
                            <p>{activity.description[1]}</p>
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
};

export default MajorActs;
