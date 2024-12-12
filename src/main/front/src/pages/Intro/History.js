import React from "react";
import "../../styles/History.css";

const History = () => {
    const historyData = [
        {
            year: "2024",
            events: ["3월 - 학회원 모집", "4월 - KUCIS 활동", "5월 - MT", "7월 - ~~~"],
        },
        {
            year: "2023",
            events: ["3월 - 학회원 모집", "4월 - KUCIS 활동", "5월 - MT", "7월 - ~~~"],
        },
        {
            year: "2022",
            events: ["3월 - 학회원 모집", "4월 - KUCIS 활동", "5월 - MT", "7월 - ~~~"],
        },
        {
            year: "2021",
            events: ["3월 - 학회원 모집", "4월 - KUCIS 활동", "5월 - MT", "7월 - ~~~"],
        },
        {
            year: "2020",
            events: ["3월 - 학회원 모집", "4월 - KUCIS 활동", "5월 - MT", "7월 - ~~~"],
        },
    ];

    return (
        <div className="history-container">
            {historyData.map((item, index) => (
                <div key={index} className="history-card">
                    <div className="year">{item.year}</div>
                    <div className="events">
                        {item.events.map((event, idx) => (
                            <p key={idx}>{event}</p>
                        ))}
                    </div>
                </div>
            ))}
        </div>
    );
};

export default History;
