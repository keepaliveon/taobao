package com.youmeng.taoshelf.web;

import com.youmeng.taoshelf.entity.Log;
import com.youmeng.taoshelf.entity.User;
import com.youmeng.taoshelf.service.LogService;
import com.youmeng.taoshelf.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class LogController {

    @Resource
    private UserService userService;

    @Resource
    private LogService logService;

    @RequestMapping("/log")
    public ModelAndView log(HttpSession session,
                            @RequestParam(defaultValue = "1") int page_no,
                            @RequestParam(defaultValue = "15") int page_size) {
        ModelAndView modelAndView = new ModelAndView("/user/log");
        User user = userService.getUserByNick((String) session.getAttribute("nick"));
        modelAndView.addObject("user", user);
        Page<Log> logPage = logService.getLogsByUser(user, page_no - 1, page_size);
        modelAndView.addObject("log_total", logPage.getTotalElements());
        modelAndView.addObject("page_size", page_size);
        modelAndView.addObject("page_no", page_no);
        List<Log> logs = logPage.getContent();
        modelAndView.addObject("logs", logs);
        return modelAndView;
    }

    @RequestMapping("/log_clear")
    public String clearLog(HttpSession session) {
        User user = userService.getUserByNick((String) session.getAttribute("nick"));
        logService.clearLogsByUser(user);
        return "redirect:/log";
    }
}
