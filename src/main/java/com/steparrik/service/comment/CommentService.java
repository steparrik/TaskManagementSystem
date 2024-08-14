package com.steparrik.service.comment;

import com.steparrik.dto.comment.CommentCreateDto;
import com.steparrik.entity.Comment;
import com.steparrik.entity.Task;
import com.steparrik.entity.User;
import com.steparrik.repository.CommentRepository;
import com.steparrik.service.task.TaskService;
import com.steparrik.service.user.UserService;
import com.steparrik.utils.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final TaskService taskService;

    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(()
                -> new ApiException("Comment not found", HttpStatus.NOT_FOUND));
    }

    public Comment createComment(String email, Long id, CommentCreateDto commentCreateDto) {
        User sender = userService.findByEmail(email);
        Task task = taskService.findById(id);

        Comment comment = new Comment();
        comment.setText(commentCreateDto.getText());
        comment.setTask(task);
        comment.setSender(sender);
        comment.setTimestamp(LocalDateTime.now());
        save(comment);

        return comment;
    }

    public void deleteComment(String email, Long id) {
        Comment comment = findById(id);
        if (comment.getTask().getOwner().getEmail().equals(email) || comment.getSender().getEmail().equals(email)) {
            commentRepository.delete(comment);
        } else {
            throw new ApiException("Insufficient rights to delete this comment", HttpStatus.BAD_REQUEST);
        }
    }


}
