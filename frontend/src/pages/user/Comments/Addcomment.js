
import React, { useState } from 'react';
import { useCookies } from 'react-cookie';
import { toast } from 'react-toastify';
import { API, MESSAGE } from '@Const';
import './style.scss';

const AddComment = ({ onAddComment }) => {
    const [content, setContent] = useState('');
    const [cookies] = useCookies(['access_token']);
    const accessToken = cookies.access_token;

    const handleContentChange = (event) => {
        setContent(event.target.value);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (content.trim() === '') {
            return;
        }
        try {
            const formData = new FormData();
            formData.append('content', content);
            const url = API.PUBLIC.ADD_COMMENT + window.location.search;
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
                body: formData,
            });
            if (response.ok) {
                const data = await response.json();
                toast.success(data.message);
                // Gọi hàm callback để cập nhật danh sách comment
                // onAddComment();
                // Reset trường nội dung comment sau khi gửi
                setContent('');
            } else {
                const data = await response.json();
                toast.error(data.message);
            }
        } catch (error) {
            toast.error(MESSAGE.DB_CONNECTION_ERROR);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <textarea
                className="comment-input"
                placeholder="Nhập nội dung comment..."
                value={content}
                onChange={handleContentChange}
            />
            <button type="submit" className="comment-submit">
                Gửi
            </button>
        </form>
    );
};

export default AddComment;
