package com.youmeng.taoshelf.service;

import com.youmeng.taoshelf.entity.Admin;
import com.youmeng.taoshelf.repository.AdminRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Transactional
@Service
public class AdminService implements UserDetailsService {

    @Resource
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Admin admin = adminRepository.findAdminByUsername(s);
        if (admin != null) {
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            ((ArrayList<GrantedAuthority>) authorities).add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return new User(admin.getUsername(), admin.getPassword(), authorities);
        } else {
            throw new UsernameNotFoundException("Username Not Found");
        }
    }

    public Admin findAdminByName(String name) {
        return adminRepository.findAdminByUsername(name);
    }

    public boolean saveAdmin(Admin admin) {
        try {
            adminRepository.save(admin);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }
}
