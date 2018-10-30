package com.youmeng.taoshelf.repository;

import com.youmeng.taoshelf.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, String> {
    Admin findAdminByUsername(String username);
}
