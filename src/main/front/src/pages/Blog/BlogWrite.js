import React, { useState } from "react";
import { writePost, updatePost } from "../../services/blogAPI"; // blogAPI.js에서 import
import "../../styles/BlogWrite.css";
import {useLocation, useNavigate} from "react-router-dom";

const BlogWrite = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const postToEdit = location.state?.post || null; // 수정할 게시물 정보
    const isMyPageEdit = location.state?.isMyPageEdit || false;  // 마이페이지에서 수정 여부

    // 상태 초기화
    const [title, setTitle] = useState(postToEdit?.title || "");
    const [contents, setContents] = useState(postToEdit?.contents || "");
    const [tag, setTag] = useState(postToEdit?.tag || "");
    const [category, setCategory] = useState(postToEdit?.category || "");
    const [image, setImage] = useState(null);
    const [preview, setPreview] = useState(null); // 이미지 미리보기 URL

    // 이미지 변경 핸들러
    const handleImageChange = (e) => {
        const file = e.target.files[0];
        setImage(file);

        // 이미지 미리보기 URL 설정
        if (file) {
            const reader = new FileReader();
            reader.onloadend = () => {
                setPreview(reader.result);
            };
            reader.readAsDataURL(file);
        } else {
            setPreview(null);
        }
    };

    // 폼 제출 핸들러
    const handleSubmit = async () => {
        if (!category) {
            alert("게시판을 선택해주세요."); // 카테고리가 선택되지 않으면 알림 표시
            return;
        }

        try {
            if (postToEdit) {
                // 수정 요청
                await updatePost({
                    boardId: postToEdit.boardId,
                    category,
                    title,
                    contents,
                    tag,
                    createAt: new Date().toISOString(),
                    image,
                });
                alert("게시물이 수정되었습니다.");
            } else {
                // 등록 요청
                const formData = new FormData();
                formData.append("category", category);
                formData.append("title", title);
                formData.append("contents", contents);
                formData.append("tag", tag);
                formData.append("roleType", "USER");
                formData.append("createAt", new Date().toISOString());
                if (image) formData.append("image", image);

                await writePost(formData);
                alert("게시물이 등록되었습니다.");
            }

            // 페이지 이동
            navigate(isMyPageEdit ? "/mypage" : "/blog");
        } catch (error) {
            console.error("글 등록/수정 실패:", error);
            alert("글 등록/수정에 실패했습니다. 다시 시도해주세요.");
        }
    };

    return (
        <div className="blog-write">
            <select
                value={category}
                onChange={(e) => setCategory(e.target.value)}
                className="category-select"
            >
                <option value="" disabled>
                    게시판 선택
                </option>
                <option value="0">공지사항</option>
                <option value="1">후기</option>
                <option value="2">활동</option>
                <option value="3">정보</option>
                <option value="4">성과물</option>
            </select>
            <input
                type="text"
                placeholder="제목을 입력하세요"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                className="title-input"
            />
            <textarea
                placeholder="내용을 입력하세요"
                value={contents}
                onChange={(e) => setContents(e.target.value)}
                className="content-input"
            />
            <div className="tag-input">
                <input
                    type="text"
                    placeholder="#태그 입력"
                    value={tag}
                    onChange={(e) => setTag(e.target.value)}
                />
            </div>
            <div className="image-upload">
                <label htmlFor="image-upload"/>
                <input
                    id="image-upload"
                    type="file"
                    accept="image/*"
                    onChange={handleImageChange}
                />
                {preview && (
                    <div className="image-preview">
                        <img src={preview} alt="미리보기"/>
                    </div>
                )}
            </div>
            <div className="buttons-container">
                <button className="submit-button" onClick={handleSubmit}>
                    {postToEdit 
                        ? (isMyPageEdit ? "수정 완료" : "수정 완료") 
                        : "완료"}
                </button>
            </div>
        </div>
    );
};

export default BlogWrite;
