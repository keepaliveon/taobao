package com.youmeng.taoshelf.repository;

import com.youmeng.taoshelf.entity.Log;
import com.youmeng.taoshelf.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, String> {

    List<Log> findLogsByUser(User user);

    Integer deleteLogsByUser(User user);

    Page<Log> findLogsByUser(User user, Pageable pageable);

}
