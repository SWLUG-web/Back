import { useState } from "react";

const useTabs = (tabs) => {
    const [activeTab, setActiveTab] = useState(tabs[0].label);

    const switchTab = (label) => {
        setActiveTab(label);
    };

    return {
        activeTab,
        switchTab,
    };
};

export default useTabs;
