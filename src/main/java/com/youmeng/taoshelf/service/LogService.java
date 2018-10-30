package com.youmeng.taoshelf.service;

import com.youmeng.taoshelf.entity.Log;
import com.youmeng.taoshelf.entity.User;
import com.youmeng.taoshelf.repository.LogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@Transactional
@Service
public class LogService {

    @Resource
    private LogRepository logRepository;

    public void clearLogsByUser(User user) {
        try {
            logRepository.deleteLogsByUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    public Page<Log> getLogsByUser(User user, int no, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "time");
        Pageable pageable = PageRequest.of(no, size, sort);
        return logRepository.findLogsByUser(user, pageable);
    }

    public void log(User user, String type, String msg) {
        Log log = new Log(user);
        log.setType(type);
        log.setMessage(msg);
        logRepository.save(log);
    }
}
