package com.steparrik.service.task;

import com.steparrik.dto.task.TaskCreateDto;
import com.steparrik.dto.task.TaskEditDto;
import com.steparrik.entity.Task;
import com.steparrik.entity.User;
import com.steparrik.entity.enums.Status;
import com.steparrik.repository.TaskRepository;
import com.steparrik.service.user.UserService;
import com.steparrik.utils.exception.ApiException;
import com.steparrik.utils.mapper.task.TaskCreateMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class TaskService {
    private final UserService userService;
    private final TaskRepository taskRepository;
    private final TaskCreateMapper taskCreateMapper;


    public void save(Task task) {
        taskRepository.save(task);
    }

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ApiException("Task not found", HttpStatus.NOT_FOUND));
    }

    public void deleteTask(Long id, String email) {
        Task task = findById(id);

        if (!task.getOwner().getEmail().equals(email)) {
            throw new ApiException("Insufficient rights to delete this task", HttpStatus.BAD_REQUEST);
        }
        taskRepository.delete(task);
    }


    public Set<Task> getTasksList(String email, boolean executeTasks) {
        User user = userService.findByEmail(email);
        Set<Task> taskList = new HashSet<>();
        if (!executeTasks) {
            taskList = user.getOwnTasks();
        } else {
            taskList = user.getExecuteTasks();
        }
        return taskList;
    }

    public Task createTask(String email, TaskCreateDto taskCreateDto) {
        Task task = taskCreateMapper.toEntity(taskCreateDto);
        User owner = userService.findByEmail(email);
        task.setOwner(owner);
        task.setExecutor(owner);
        save(task);
        return task;
    }

    public Task editTask(String email, Long id, TaskEditDto taskEditDto) {
        Task task = findById(id);

        if (email.equals(task.getOwner().getEmail())) {
            return editTaskOwner(task, taskEditDto);
        } else if (email.equals(task.getExecutor().getEmail())) {
            return editTaskExecutor(task, taskEditDto);
        } else {
            throw new ApiException("Insufficient rights to edit this task", HttpStatus.BAD_REQUEST);
        }
    }

    public Task editTaskOwner(Task task, TaskEditDto taskEditDto) {
        if (taskEditDto.getExecutor() != null && taskEditDto.getExecutor().getEmail() != null) {
            User newExecutor = userService.findByEmail(taskEditDto.getExecutor().getEmail());
            task.setExecutor(newExecutor);
        }
        if (taskEditDto.getTitle() != null && !taskEditDto.getTitle().isEmpty()) {
            task.setTitle(taskEditDto.getTitle());
        }
        if (taskEditDto.getDescription() != null && !taskEditDto.getDescription().isEmpty()) {
            task.setDescription(taskEditDto.getDescription());
        }
        if (taskEditDto.getStatus() != null) {
            task.setStatus(taskEditDto.getStatus());
        }
        if (taskEditDto.getPriority() != null) {
            task.setPriority(taskEditDto.getPriority());
        }
        save(task);
        return task;
    }

    public Task editTaskExecutor(Task task, TaskEditDto taskEditDto) {
        Status status = taskEditDto.getStatus();
        if (status == null) {
            throw new ApiException("The executor can only change the STATUS of the task, so fill out this field if you want to change the task", HttpStatus.BAD_REQUEST);
        }
        task.setStatus(status);
        save(task);

        return task;
    }


    public int countCommentsWithCurrentUser(Long userId){
         return taskRepository.countTasksByUserId(userId);
    }

}
