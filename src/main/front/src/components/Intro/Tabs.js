import React from "react";
import useTabs from "../../hooks/useTabs"; // 커스텀 훅 import
import "../../styles/Tabs.css";

const Tabs = ({ tabs }) => {
    const { activeTab, switchTab } = useTabs(tabs);

    return (
        <div className="tabs">
            <div className="tab-buttons">
                {tabs.map((tab) => (
                    <button
                        key={tab.label}
                        className={`tab-button ${activeTab === tab.label ? "active" : ""}`}
                        onClick={() => switchTab(tab.label)}
                    >
                        {tab.label}
                    </button>
                ))}
            </div>
            <div className="tab-content">
                {tabs.map((tab) =>
                    activeTab === tab.label ? <tab.component key={tab.label} /> : null
                )}
            </div>
        </div>
    );
};

export default Tabs;
