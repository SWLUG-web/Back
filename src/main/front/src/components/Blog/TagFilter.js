import React from "react";
import "../../styles/TagFilter.css";

const TagFilter = ({ tags, selectedTag, setSelectedTag }) => {
    const handleTagClick = (tag) => {
        if (selectedTag === tag) {
            setSelectedTag(""); // 동일 태그를 클릭하면 선택 해제
        } else {
            setSelectedTag(tag); // 다른 태그를 클릭하면 선택
        }
    };

    return (
        <div className="tag-list">
            {tags.map((tag) => (
                <button
                    key={tag}
                    className={`tag-button ${selectedTag === tag ? "active" : ""}`}
                    onClick={() => handleTagClick(tag)}
                >
                    #{tag}
                </button>
            ))}
        </div>
    );
};

export default TagFilter;
