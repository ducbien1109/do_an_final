package com.FashionStore.models;

import jakarta.persistence.*;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentID;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users userID;

    @Column(name = "created_at")
    private String createdAt;

    public Comment() {
    }

    public Comment(Long commentID, String content, Users userID, String createdAt) {
        this.commentID = commentID;
        this.content = content;
        this.userID = userID;
        this.createdAt = createdAt;
    }

    public Long getCommentID() {
        return commentID;
    }

    public void setCommentID(Long commentID) {
        this.commentID = commentID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Users getUserID() {
        return userID;
    }

    public void setUserID(Users users) {
        this.userID = users;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
