import React from "react";
import "../../styles/CIIntro.css";

const CllIntro = () => {
    return (
        <div className="ci-intro">
            <div className="header">
                <h2>
                    📌 SWLUG 슈러그 기본 로고
                    <a href="/Logo4.jpeg" download className="download-button">다운로드</a>
                </h2>
            </div>
            <div className="logo-section">
                <div className="image-container">
                    <img src="/Logo4.jpeg" alt="SWLUG 기본 로고" className="logo-image"/>
                </div>
            </div>

            <div className="header">
                <h2>
                    📌 SWLUG 슈러그 공식 캐릭터
                    <a href="/sub_logo1.png" download className="download-button">다운로드</a>
                </h2>
            </div>
            <div className="logo-section">
                <div className="image-container">
                    <img src="/sub_logo1.png" alt="SWLUG 공식 캐릭터" className="logo-image"/>
                </div>
            </div>
        </div>


    );
};

export default CllIntro;
