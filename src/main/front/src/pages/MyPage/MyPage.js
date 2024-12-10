import React, { useState, useEffect } from "react";
import { useSelector } from 'react-redux';
import { getUserInfo, deletePost } from "../../services/api"
import Info from "../../components/MyPage/MyPageInfo";
import "../../styles/MyPage.css";
import {useNavigate} from "react-router-dom";

function MyPage() {
  const [posts, setPosts] = useState([]); // 모든 게시물
  const [currentPage, setCurrentPage] = useState(1); // 현재 페이지
  const postsPerPage = 7; // 한 페이지당 게시물 수
  const [totalPage, setTotalPage] = useState(1); // 전체 페이지 수
  const navigate = useNavigate();

  const user = useSelector(state => state.auth.user);
  const localUser = JSON.parse(localStorage.getItem('user'));

  useEffect(() => {
    const fetchUserPosts = async () => {
      try {
        const currentUser = user || localUser;
        if (currentUser) {
          const response = await getUserInfo({
            id: currentUser.id,
            pw: currentUser.pw
          });
          
          if (response.status === 200) {
            setPosts(response.data.board);
            setTotalPage(Math.ceil(response.data.board.length / postsPerPage));
          }
        }
      } catch (error) {
        console.error('게시물 불러오기 실패:', error);
      }
    };

    fetchUserPosts();
  }, [user, localUser]);

  // 게시물 삭제 처리
  const handleDelete = async (boardId) => {
    try {
      const response = await deletePost('mypage', { boardId }); // 'mypage'로 API 요청
      if (response.status === 200) {
        const updatedPosts = posts.filter((post) => post.boardId !== boardId);
        setPosts(updatedPosts);
        setTotalPage(Math.ceil(updatedPosts.length / postsPerPage)); // 삭제 후 총 페이지 수 재계산
        alert('게시물이 성공적으로 삭제되었습니다.');
      } else {
        console.error('삭제 실패:', response);
        alert('게시물 삭제에 실패했습니다.');
      }
    } catch (error) {
      console.error('게시물 삭제 중 오류 발생:', error);
      alert('게시물 삭제 중 오류가 발생했습니다.');
    }
  };  

  const handleEdit = (post) => {
    navigate('/board/write', { 
        state: { 
            post, 
            isMyPageEdit: true
        } 
    });
};

  // 페이지 변경 처리
  const handlePageChange = (page) => {
    if (page < 1 || page > totalPage) return; // 페이지 범위 제한
    setCurrentPage(page);
  };

  // 현재 페이지에 해당하는 게시물 가져오기
  const currentPosts = posts.slice(
    (currentPage - 1) * postsPerPage,
    currentPage * postsPerPage
  );

  return (
    <div className="mypage">
      <h1 className="form_title">마이 페이지</h1>

      {/* 회원 정보 */}
      <Info />

      {/* 게시물 목록 */}
      <section className="user-posts">
        <h2 className="form_subtitle">작성한 글</h2>
        <div className="posts-list">
          {currentPosts.length > 0 ? (
            currentPosts.map((post) => (
              <div className="post-item" key={post.boardId}>
                <span>{post.boardId}</span>
                <span>{post.title}</span>
                <span>{post.date}</span>
                <button onClick={() => handleEdit(post)}>수정</button>
                <button onClick={() => handleDelete(post.boardId)}>삭제</button>
              </div>
            ))
          ) : (
            <div className="no-posts">게시물이 없습니다.</div>
          )}
        </div>

        {/* 페이지네이션 */}
        <div className="pagination">
          <button onClick={() => handlePageChange(1)} disabled={currentPage === 1}>
            &lt;&lt; {/* 첫 페이지 */}
          </button>
          <button onClick={() => handlePageChange(currentPage - 1)} disabled={currentPage === 1}>
            &lt; {/* 이전 페이지 */}
          </button>
          {[...Array(totalPage)].map((_, index) => (
            <button
              key={index}
              onClick={() => handlePageChange(index + 1)}
              className={currentPage === index + 1 ? "active" : ""}
            >
              {index + 1}
            </button>
          ))}
          <button onClick={() => handlePageChange(currentPage + 1)} disabled={currentPage === totalPage}>
            &gt; {/* 다음 페이지 */}
          </button>
          <button onClick={() => handlePageChange(totalPage)} disabled={currentPage === totalPage}>
            &gt;&gt; {/* 마지막 페이지 */}
          </button>
        </div>
      </section>
    </div>
  );
}

export default MyPage;
