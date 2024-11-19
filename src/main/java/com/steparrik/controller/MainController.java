package com.steparrik.controller;

import com.steparrik.dto.comment.CommentCreateDto;
import com.steparrik.dto.comment.CommentResponseDto;
import com.steparrik.dto.task.TaskCreateDto;
import com.steparrik.dto.task.TaskEditDto;
import com.steparrik.dto.task.TaskResponseDto;
import com.steparrik.dto.user.UserAuthDto;
import com.steparrik.dto.user.UserRegistrationDto;
import com.steparrik.entity.User;
import com.steparrik.service.comment.CommentService;
import com.steparrik.service.task.TaskService;
import com.steparrik.service.user.UserAuthService;
import com.steparrik.service.user.UserRegistrationService;
import com.steparrik.service.user.UserService;
import com.steparrik.utils.mapper.comment.CommentResponseMapper;
import com.steparrik.utils.mapper.task.TaskResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
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
    private final UserService userService;


    @PostMapping("/auth")
    @Operation(summary = "Выдает токен")
    public ResponseEntity<?> authentication(@Valid @RequestBody UserAuthDto authRequest) {
        return ResponseEntity.ok().body(userAuthService.createAuthToken(authRequest));
    }

    @PostMapping("/registration")
    public void registration(@Valid @RequestBody UserRegistrationDto userRegistrationDto) {
        userRegistrationService.registration(userRegistrationDto);
    }

    @GetMapping("/tasks")
    public List<TaskResponseDto> getTasks(Principal principal,
                                          @RequestParam(required = false) String email,
                                          @RequestParam(required = false) boolean executeTasks) {
        return taskService.getTasksList(principal, email, executeTasks).stream().map(taskResponseMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/tasks/{taskId}")
    public TaskResponseDto getDefiniteTask(@PathVariable Long taskId) {
        return taskResponseMapper.toDto(taskService.findById(taskId));
    }

    @PostMapping("/tasks")
    public TaskResponseDto createTask(Principal principal, @Valid @RequestBody TaskCreateDto taskCreateDto) {
        String email = principal.getName();
        return taskResponseMapper.toDto(taskService.createTask(email, taskCreateDto));
    }

    @PutMapping("/tasks/{taskId}")
    public TaskResponseDto editTask(@PathVariable Long taskId, Principal principal, @RequestBody TaskEditDto taskEditDto) {
        String email = principal.getName();
        return taskResponseMapper.toDto(taskService.editTask(email, taskId, taskEditDto));
    }

    @DeleteMapping("/tasks/{taskId}")
    public void deleteTask(@PathVariable Long taskId, Principal principal) {
        String email = principal.getName();
        taskService.deleteTask(taskId, email);
    }


    @PostMapping("/tasks/{taskId}/comment")
    public CommentResponseDto createComment(@PathVariable Long taskId, Principal principal, @RequestBody CommentCreateDto commentCreateDto) {
        String email = principal.getName();
        return commentResponseMapper.toDto(commentService.createComment(email, taskId, commentCreateDto));
    }

    @DeleteMapping("/tasks/comment/{commentId}")
    public void createComment(@PathVariable Long commentId, Principal principal) {
        String email = principal.getName();
        commentService.deleteComment(email, commentId);
    }

    @GetMapping("/tasksCount")
    public int getTasks(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        return taskService.countCommentsWithCurrentUser(user.getId());
    }

}
