import React, { useEffect, useState } from 'react';
import '../../styles/ApplyPage.css';

const ApplyPage = () => {
    const [isApply, setIsApply] = useState(null);

    useEffect(() => {
        fetch("/apply")
            .then(res => {
                if (!res.ok) {
                    throw new Error(`HTTP error! status: ${res.status}`);
                }
                return res.json();  // JSON으로 파싱 시도
            })
            .then(data => {
                console.log("Received data:", data);  // 응답 확인
                if (data && data.apply !== undefined) {
                    setIsApply(data.apply);  // isApply를 data.apply로 설정
                }
            })
            .catch(err => {
                console.error("Failed to fetch data:", err);
                setIsApply(false);  // 오류가 발생하면 isApply를 false로 설정
            });
    }, []);

    if (isApply === null) {
        return <div>로딩 중...</div>;
    } else {
        return (
            <div
                className="apply-page container mx-auto px-4 py-8"
                style={{
                    backgroundImage: 'url(/apply_back.png)',
                    backgroundSize: 'cover',
                    backgroundPosition: 'center',
                }}
            >
                <h1 className="apply-title text-3xl font-bold text-center mb-6" style={{ fontSize: '24px' }}>
                    지원
                </h1>
                <div className="apply-content">
                    <img src="/apply_swlug.png" alt="SWLUG" className="apply-logo mx-auto" />

                    {isApply ? (
                        <>
                            <p className="apply-description text-center">
                                서울여자대학교 정보보호학과 소학회 SWLUG에서 30기 신입학회를 모집합니다.
                            </p>
                            <p className="apply-period text-center text-lg">
                                모집 기간: ~ 2024.03.03 (일)
                            </p>
                            <div className="apply-buttons">
                                <a href="/intro" className="apply-button learn-more">
                                    SWLUG에 대해 자세히 알아보기
                                </a>
                                <a
                                    href="https://docs.google.com/forms/d/e/1FAIpQLSeStCL0uIPF7YaULhSaUu34ge_Y8JPGvrBZtKcydOBExu0LHQ/closedform"
                                    className="apply-button apply-now"
                                >
                                    SWLUG 신입 학회원 지원하러 가기
                                </a>
                            </div>
                            {/* Additional Info... */}
                        </>
                    ) : (
                        <p className="apply-description text-center">지원 기간이 아닙니다.</p>
                    )}
                </div>
            </div>
        );
    }
};

export default ApplyPage;
