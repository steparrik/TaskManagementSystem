package com.steparrik.repository;

import com.steparrik.entity.Task;
import com.steparrik.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByExecutor(User executor);
}
