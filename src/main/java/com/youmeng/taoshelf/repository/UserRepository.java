package com.youmeng.taoshelf.repository;

import com.youmeng.taoshelf.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    Page<User> findUsersByDeletedIsFalse(Pageable pageable);

    User findUserByDeletedIsFalseAndNickIs(String nick);

    Page<User> findUsersByDeletedIsFalseAndNickContains(String s, Pageable pageable);

}
