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

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product productID;

    @Column(name = "detete")
    private boolean detete;

    public Comment() {
    }

    public Comment(Long commentID, String content, Users userID, String createdAt, Product productID, boolean detete) {
        this.commentID = commentID;
        this.content = content;
        this.userID = userID;
        this.createdAt = createdAt;
        this.productID = productID;
        this.detete = detete;
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

    public Product getProductID() {
        return productID;
    }

    public void setProductID(Product productID) {
        this.productID = productID;
    }

    public boolean isDetete() {
        return detete;
    }

    public void setDetete(boolean detete) {
        this.detete = detete;
    }
}
