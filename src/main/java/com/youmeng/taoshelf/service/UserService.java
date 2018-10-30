package com.youmeng.taoshelf.service;

import com.youmeng.taoshelf.entity.User;
import com.youmeng.taoshelf.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;

@Transactional
@Service
public class UserService {

    @Resource
    private UserRepository userRepository;

    public Boolean saveUser(User user) {
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    public Boolean deleteUserByNick(String nick) {
        try {
            User user = userRepository.findUserByDeletedIsFalseAndNickIs(nick);
            user.setDeleted(true);
            user.setSessionKey1(null);
            user.setSessionKey2(null);
            user.setEndTime(new Date());
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    public User getUserByNick(String nick) {
        return userRepository.findUserByDeletedIsFalseAndNickIs(nick);
    }

    public Page<User> getAllUsers(int size, int no, String s) {
        Sort sort = new Sort(Sort.Direction.DESC, "joinTime");
        Pageable pageable = PageRequest.of(no, size, sort);
        if (s.equals("")) {
            return userRepository.findUsersByDeletedIsFalse(pageable);
        } else {
            return userRepository.findUsersByDeletedIsFalseAndNickContains(s, pageable);
        }
    }

}
