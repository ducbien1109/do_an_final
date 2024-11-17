package com.FashionStore.repositories;

import com.FashionStore.models.Comment;
import com.FashionStore.models.Product;
import com.FashionStore.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByUserID(Users userID);
    List<Comment> findByCommentID(Comment comment);
    List<Comment> findByProductID(Product product);

//    @Query("SELECT * FROM comments c WHERE c.product_id = :productID AND c.detete = 0")
//    List<Comment> findCommentByProductId(Long productID);
}
