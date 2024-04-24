import React from 'react';
import './style.scss'; // Import stylesheet


const Comment = ({ author, content, createAt }) => {
    return (
        <div className="comment">
            <div className="avatar"></div>
            <div className="comment-content">
                <div className="author">{author}Nguyễn Danh Khánh</div>
                <div className="content">{content}áo chat</div>
                <div className="content">{createAt}18-09-2004</div>
            </div>
        </div>
    );
};

export default Comment;
