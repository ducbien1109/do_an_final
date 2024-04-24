package com.FashionStore.repositories;

import com.FashionStore.models.Comment;
import com.FashionStore.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByUserID(Users userID);
    List<Comment> findByCommentID(Comment comment);

}
