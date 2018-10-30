package com.youmeng.taoshelf.repository;

import com.youmeng.taoshelf.entity.Task;
import com.youmeng.taoshelf.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, String> {

    Page<Task> findTasksByUser(User user, Pageable pageable);

    List<Task> findTasksByUser(User user);

    Task findTaskById(String id);

    Page<Task> findTasksByStatusContains(String s, Pageable pageable);
}
