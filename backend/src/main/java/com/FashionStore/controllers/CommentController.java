package com.FashionStore.controllers;

import com.FashionStore.models.Comment;
import com.FashionStore.models.Users;
import com.FashionStore.repositories.CommentRepository;
import com.FashionStore.repositories.UsersRepository;
import com.FashionStore.security.JwtTokenUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("${api.base-path}")
public class CommentController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Value("${param.content}")
    private String contents;

    @Value("${header.authorization}")
    private String headerAuthorization;

    @Value("${authorization.bearer}")
    private String authorizationBearer;
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

        Users user = usersRepository.findUsersByEmail(email);
        Users users = usersRepository.findUsersByUserID(user.getUserID());
        Long userID = users.getUserID();
        if (user == null || !Objects.equals(user.getUserID(), userID)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Comment comment = new Comment();
        comment.setUserID(user);
        comment.setContent(content);
        comment.setCreatedAt(String.valueOf(commentDate));

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

        List<Comment> comments = commentRepository.findAll();

        return ResponseEntity.ok(comments);
    }
}
