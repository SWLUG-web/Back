import React, { useEffect, useState } from 'react';
import FAQItem from '../../components/FAQ/FAQItem';
import faqs from '../../data/faqs';
import '../../styles/FAQPage.css';

const FAQPage = () => {
    // 카테고리 목록
    const categories = ['전체', '지원', '활동', '기타'];

    // 상태 관리
    const [categoryIndex, setCategoryIndex] = useState(0); // 초기값을 '전체'로 설정 (인덱스: 0)

    useEffect(() => {
        fetch("/faq") // 서버에 요청
            .then((res) => {
                if (!res.ok) {
                    throw new Error(`HTTP error! status: ${res.status}`); // 에러 처리
                }
                return res.json(); // JSON 형식으로 파싱
            })
            .then((c) => {
                console.log("Received category index from server:", c);
                setCategoryIndex(c); // 서버에서 받은 값으로 카테고리 상태 업데이트
            })
            .catch((err) => console.error("Failed to fetch category index:", err)); // 에러 처리
    }, []);

    // FAQ 데이터 필터링
    const filteredFaqs =
        categoryIndex === 0
            ? faqs // '전체' 카테고리는 모든 데이터를 표시
            : faqs.filter((faq) => faq.category === categories[categoryIndex]); // 선택된 카테고리에 해당하는 데이터 필터링

    return (
        <div className="faq-page container mx-auto px-4 py-8">
            {/* 페이지 제목 */}
            <h1 className="apply-title text-3xl font-bold text-center mb-6" style={{ fontSize: '24px' }}>
                FAQ
            </h1>

            {/* 카테고리 필터 버튼 */}
            <div className="faq-filters">
                {categories.map((cat, index) => (
                    <button
                        key={index}
                        onClick={() => setCategoryIndex(index)} // 버튼 클릭 시 선택된 인덱스로 상태 업데이트
                        className={`faq-filter-button ${
                            categoryIndex === index ? 'selected' : '' // 현재 선택된 인덱스에 따라 스타일 변경
                        }`}
                    >
                        {cat}
                    </button>
                ))}
            </div>

            {/* 필터링된 FAQ 항목 출력 */}
            {filteredFaqs.length > 0 ? (
                filteredFaqs.map((faq) => (
                    <FAQItem key={faq.id} question={faq.question} answer={faq.answer} />
                ))
            ) : (
                <p className="no-faqs-text text-center mt-4">해당 카테고리에 대한 FAQ가 없습니다.</p>
            )}

            {/* 페이지 하단 텍스트 */}
            <p className="faq-footer-text">
                ※ 이 밖의 문의사항은 Contact Us를 참고해 문의 바랍니다.
            </p>
        </div>
    );
};

export default FAQPage;
