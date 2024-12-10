import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import {deletePost, fetchAdjacentPosts, fetchPostDetail} from "../../services/blogAPI";
import "../../styles/BlogPost.css";

const BlogPost = () => {
    const { boardId } = useParams(); // URL에서 게시물 ID 추출
    const navigate = useNavigate();
    const [post, setPost] = useState(null); // 게시물 데이터 저장
    const [loading, setLoading] = useState(true); // 로딩 상태
    const [adjacentPosts, setAdjacentPosts] = useState({ previous: null, next: null }); // 이전/다음 글 데이터

    useEffect(() => {
        const fetchData = async () => {
            try {
                const data = await fetchPostDetail(boardId); // 서버에서 게시물 상세 데이터 가져오기
                setPost(data);

                const adjacentData = await fetchAdjacentPosts(boardId); // 이전/다음 글 데이터 가져오기
                setAdjacentPosts(adjacentData);

                setLoading(false);
            } catch (error) {
                console.error("게시물 데이터를 가져오는 중 오류:", error);
                setLoading(false);
            }
        };
        fetchData();
    }, [boardId]);

    const handleNavigate = (id) => {
        navigate(`/board/post/${id}`);
    };

    const handleDelete = async () => {
        if (window.confirm("정말 삭제하시겠습니까?")) {
            try {
                await deletePost(boardId); // 게시물 삭제 요청
                alert("게시물이 삭제되었습니다.");
                navigate("/blog");
            } catch (error) {
                console.error("게시물 삭제 실패:", error);
                alert("게시물 삭제에 실패했습니다. 다시 시도해주세요.");
            }
        }
    };

    const handleEdit = () => {
        navigate("/board/write", { state: { post } }); // 게시물 데이터를 전달하며 글쓰기 페이지로 이동
    };

    if (loading) return <p>Loading...</p>;

    if (!post) return <p>게시물을 찾을 수 없습니다.</p>;

    const categoryMapping = {
    "0": "공지사항",
    "1": "후기",
    "2": "활동",
    "3": "정보",
    "4": "성과물",
};


    return (
        <div className="blog-post">
            <div className="post-category">{categoryMapping[post.category]}</div>
            <h1>{post.title}</h1>
            <div className="post-actions">
                <button className="edit-button" onClick={handleEdit}>
                    수정
                </button>
                <span className="divider">|</span>
                <button className="delete-button" onClick={handleDelete}>
                    삭제
                </button>
            </div>
            <p className="post-date">{new Date(post.date).toLocaleDateString("ko-KR")}</p>
            {/* 항상 동일한 이미지 표시 */}
            <img src="/Logo5.png" alt="Default Logo" />

            <div className="post-content">
                {post.content.split("\n").map((line, index) => (
                    <p key={index}>{line}</p>
                ))}
            </div>
            <div className="post-tags">
                {post.tags.map((tag, index) => (
                    <span key={index} className="tag">
                        #{tag}
                    </span>
                ))}
            </div>

            {/* 이전 글/다음 글 네비게이션 */}
            <div className="navigation">
                <button
                    onClick={() => adjacentPosts.previous && handleNavigate(adjacentPosts.previous.id)}
                    disabled={!adjacentPosts.previous}
                >
                    {adjacentPosts.previous ? `< 이전글: ${adjacentPosts.previous.title}` : "글이 없습니다"}
                </button>
                <button
                    onClick={() => adjacentPosts.next && handleNavigate(adjacentPosts.next.id)}
                    disabled={!adjacentPosts.next}
                >
                    {adjacentPosts.next ? `다음글: ${adjacentPosts.next.title} >` : "글이 없습니다"}
                </button>
            </div>
        </div>
    );
};

export default BlogPost;
