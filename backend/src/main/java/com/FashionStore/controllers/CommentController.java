package com.FashionStore.controllers;

import com.FashionStore.models.Comment;
import com.FashionStore.models.Product;
import com.FashionStore.models.Users;
import com.FashionStore.repositories.CommentRepository;
import com.FashionStore.repositories.ProductRepository;
import com.FashionStore.repositories.UsersRepository;
import com.FashionStore.security.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("${api.base-path}")
public class CommentController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Value("${comment.param.content}")
    private String contents;

    @Value("${header.authorization}")
    private String headerAuthorization;

    @Value("${authorization.bearer}")
    private String authorizationBearer;

    @Value("${comment.param.userID}")
    private String usersID;

    @Value("${param.productID}")
    private String PARAM_PRODUCT_ID;

    @Value("${comment.param.commentID}")
    private String COMMENT_ID;

    @PostMapping("${endpoint.public.add.comment}")
    public ResponseEntity<?> addComment(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        accessToken = accessToken.replace("Bearer ", "");
        if (!jwtTokenUtil.isTokenValid(accessToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = jwtTokenUtil.getEmailFromToken(accessToken);

        String content = request.getParameter(contents);
        Date commentDate = new Date();
        commentDate.setTime(commentDate.getTime());

        Long productID = Long.valueOf(request.getParameter("productID"));
        Product product = productRepository.findProductByProductID(productID);
        Users user = usersRepository.findUsersByEmail(email);
        if (user == null || !Objects.equals(user.getUserID(), user.getUserID())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String.valueOf(request.getParameter(content));
        Comment comment = new Comment();
        comment.setUserID(user);
        comment.setContent(content);
        comment.setCreatedAt(String.valueOf(commentDate));
        comment.setProductID(product);
        comment.setDetete(false);

        commentRepository.save(comment);

        return ResponseEntity.ok(comment);
    }
    @GetMapping("${endpoint.public.get-comment}")
    public ResponseEntity<?> getComments(HttpServletRequest request) {
        String accessToken = request.getHeader(headerAuthorization);
        accessToken = accessToken.replace(authorizationBearer, "");
        if (!jwtTokenUtil.isTokenValid(accessToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Comment> comments = new ArrayList<>();
        if (request.getParameter("productID") != null) {
            Long productID = Long.valueOf(request.getParameter("productID"));
            Product product = productRepository.findProductByProductID(Long.valueOf(productID));
            comments = commentRepository.findByProductID(product);
        } else {
            comments = commentRepository.findAll();
        }

        return ResponseEntity.ok(comments);
    }

    @PostMapping("${endpoint.public.delete-comment}")
    public ResponseEntity<?> deleteComment(HttpServletRequest request) {
        try {
            String accessToken = request.getHeader(headerAuthorization);
            accessToken = accessToken.replace(authorizationBearer, "");
            if (!jwtTokenUtil.isTokenValid(accessToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            String email = jwtTokenUtil.getEmailFromToken(accessToken);
            boolean isAdmin = usersRepository.findUsersByEmail(email).getIsAdmin();
            Long commentId = Long.valueOf(request.getParameter(COMMENT_ID));
            Optional<Comment> commentOptional = commentRepository.findById(commentId);
            if (!commentOptional.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Comment comment = commentOptional.get();
            if (!isAdmin && !Objects.equals(comment.getUserID().getEmail(), email)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            commentRepository.delete(comment);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("${endpoint.public.edit-comment}")
    public ResponseEntity<?> editComment(HttpServletRequest request) {
        try {
            String accessToken = request.getHeader(headerAuthorization);
            accessToken = accessToken.replace(authorizationBearer, "");
            if (!jwtTokenUtil.isTokenValid(accessToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            String email = jwtTokenUtil.getEmailFromToken(accessToken);
            boolean isAdmin = usersRepository.findUsersByEmail(email).getIsAdmin();
            Long commentId = Long.valueOf(request.getParameter(COMMENT_ID));
            Optional<Comment> commentOptional = commentRepository.findById(commentId);
            if (!commentOptional.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Comment comment = commentOptional.get();
            if (!isAdmin && !Objects.equals(comment.getUserID().getEmail(), email)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            String content = request.getParameter(contents);
            comment.setContent(content);
            commentRepository.save(comment);

            return ResponseEntity.ok(comment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
