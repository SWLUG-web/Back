export const fetchPosts = async (page, size, tag = "", query = "", category = "") => {
    let url = `/blog?page=${page}&size=${size}`;

    // 카테고리, 태그, 검색어가 있을 경우 쿼리 파라미터 추가
    if (tag) url += `&tag=${tag}`;
    if (query) url += `&query=${query}`;
    if (category) url += `&category=${category}`;

    const response = await fetch(url);

    if (!response.ok) {
        throw new Error("게시물 데이터를 가져오는 데 실패했습니다.");
    }

    return response.json();
};

export const fetchPostDetail = async (boardId) => {
    const response = await fetch(`/board/view/${boardId}`);
    if (!response.ok) {
        throw new Error("게시물 상세 데이터를 가져오는 데 실패했습니다.");
    }
    return response.json();
};

export const fetchAdjacentPosts = async (boardId) => {
    const response = await fetch(`/board/adjacent/${boardId}`);
    if (!response.ok) {
        throw new Error("이전/다음 글 데이터를 가져오는 데 실패했습니다.");
    }
    return response.json();
};


export const writePost = async (formData) => {
    const response = await fetch("/board/save", {
        method: "POST",
        body: formData, // FormData 객체 전달
    });

    if (!response.ok) {
        throw new Error("글 등록 실패");
    }

    return response.json();
};


export const searchPosts = async (searchQuery, tag) => {
    const response = await fetch(`/board/search?search=${searchQuery}&tag=${tag}`);
    if (!response.ok) {
        throw new Error("검색 결과를 가져오는 데 실패했습니다.");
    }
    return response.json();
};

export const updatePost = async (post) => {
    const response = await fetch("/board/update", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(post),
    });

    if (!response.ok) {
        throw new Error("게시물 수정 실패");
    }

    return response.json();
};


export const deletePost = async (boardId) => {
    const response = await fetch(`/board/delete`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ boardId }),
    });

    if (!response.ok) {
        throw new Error("게시물 삭제 실패");
    }

    return response.json();
};
