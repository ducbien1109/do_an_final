import React, { useState } from 'react';
import './style.scss'; // Import stylesheet

const AddComment = ({ onAddComment }) => {
    const [content, setContent] = useState('');

    const handleContentChange = (event) => {
        setContent(event.target.value);
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        // Kiểm tra nội dung comment không được rỗng trước khi gửi
        if (content.trim() === '') {
            return;
        }
        // Gọi hàm callback để gửi comment lên server
        onAddComment(content);
        // Reset trường nội dung comment sau khi gửi
        setContent('');
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
