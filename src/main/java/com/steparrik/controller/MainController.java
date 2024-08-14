package com.steparrik.controller;

import com.steparrik.dto.comment.CommentCreateDto;
import com.steparrik.dto.task.TaskCreateDto;
import com.steparrik.dto.task.TaskEditDto;
import com.steparrik.dto.user.UserAuthDto;
import com.steparrik.dto.user.UserRegistrationDto;
import com.steparrik.service.comment.CommentService;
import com.steparrik.service.task.TaskService;
import com.steparrik.service.user.UserAuthService;
import com.steparrik.service.user.UserRegistrationService;
import com.steparrik.utils.mapper.comment.CommentResponseMapper;
import com.steparrik.utils.mapper.task.TaskResponseMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
@Tag(name = "Task Management System Main Controller")
public class MainController {
    private final UserAuthService userAuthService;
    private final UserRegistrationService userRegistrationService;
    private final TaskService taskService;
    private final CommentService commentService;
    private final TaskResponseMapper taskResponseMapper;
    private final CommentResponseMapper commentResponseMapper;


    @PostMapping("/auth")
    public ResponseEntity<?> authentication(@Valid @RequestBody UserAuthDto authRequest) {
        return ResponseEntity.ok().body(userAuthService.createAuthToken(authRequest));
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        return userRegistrationService.registration(userRegistrationDto);
    }

    @GetMapping("/tasks")
    public ResponseEntity<?> getTasks(Principal principal,
                                      @RequestParam(required = false) String email,
                                      @RequestParam(required = false) boolean executeTasks) {
        if (email == null || email.isEmpty()) {
            email = principal.getName();
        }

        return ResponseEntity.ok().body(taskService.getTasksList(email, executeTasks).stream().map(taskResponseMapper::toDto).collect(Collectors.toList()));
    }

    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<?> getDefiniteTask(@PathVariable Long taskId) {
        return ResponseEntity.ok().body(taskResponseMapper.toDto(taskService.findById(taskId)));
    }

    @PostMapping("/tasks")
    public ResponseEntity<?> createTask(Principal principal, @Valid @RequestBody TaskCreateDto taskCreateDto) {
        String email = principal.getName();
        return ResponseEntity.ok().body(taskResponseMapper.toDto(taskService.createTask(email, taskCreateDto)));
    }

    @PutMapping("/tasks/{taskId}")
    public ResponseEntity<?> editTask(@PathVariable Long taskId, Principal principal, @RequestBody TaskEditDto taskEditDto) {
        String email = principal.getName();
        return ResponseEntity.ok().body(taskResponseMapper.toDto(taskService.editTask(email, taskId, taskEditDto)));
    }

    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId, Principal principal) {
        String email = principal.getName();
        taskService.deleteTask(taskId, email);
        return ResponseEntity.ok().body(null);
    }


    @PostMapping("/tasks/{taskId}/comment")
    public ResponseEntity<?> createComment(@PathVariable Long taskId, Principal principal, @RequestBody CommentCreateDto commentCreateDto) {
        String email = principal.getName();
        return ResponseEntity.ok().body(commentResponseMapper.toDto(commentService.createComment(email, taskId, commentCreateDto)));
    }

    @DeleteMapping("/tasks/comment/{commentId}")
    public ResponseEntity<?> createComment(@PathVariable Long commentId, Principal principal) {
        String email = principal.getName();
        commentService.deleteComment(email, commentId);
        return ResponseEntity.ok().body(null);
    }

}
