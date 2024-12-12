import React from "react";
import IntroHeader from "../../components/Intro/IntroHeader";
import Tabs from "../../components/Intro/Tabs";
import MajorActs from "./MajorActs";
import History from "./History";
import Awards from "./Awards";
import CIIntro from "./CIIntro";

const Intro = () => {
    const tabs = [
        { label: "주요 활동", component: MajorActs },
        { label: "연혁", component: History },
        { label: "수상 경력", component: Awards },
        { label: "로고 소개", component: CIIntro },
    ];

    return (
        <div>
            <IntroHeader />
            <Tabs tabs={tabs} />
        </div>
    );
};

export default Intro;
