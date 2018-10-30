package com.youmeng.taoshelf.web;

import com.youmeng.taoshelf.entity.Good;
import com.youmeng.taoshelf.entity.Result;
import com.youmeng.taoshelf.entity.User;
import com.youmeng.taoshelf.service.GoodService;
import com.youmeng.taoshelf.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class GoodController {

    @Resource
    private UserService userService;

    @Resource
    private GoodService goodService;

    @RequestMapping("/result")
    public ModelAndView result(HttpSession session,
                               @RequestParam(defaultValue = "") String q,
                               @RequestParam String type,
                               @RequestParam(defaultValue = "1") Long page_no,
                               @RequestParam(defaultValue = "18") Long page_size) {
        if (q.equals("")) {
            q = null;
        }
        if (page_no == null) {
            page_no = 1L;
        }
        ModelAndView modelAndView = new ModelAndView("/user/result");
        modelAndView.addObject("page_no", page_no);
        modelAndView.addObject("page_size", page_size);
        modelAndView.addObject("q", q);
        modelAndView.addObject("type", type);
        User user = userService.getUserByNick((String) session.getAttribute("nick"));
        modelAndView.addObject("user", user);
        if (type.equals("instock")) {
            Result<Good> result = goodService.getGoodsInstock(user, q, page_size, page_no, 1);
            List<Good> goodList = result.getItems();
            modelAndView.addObject("goods", goodList);
            modelAndView.addObject("result_total", result.getTotal());
        } else if (type.equals("onsale")) {
            Result<Good> result = goodService.getGoodsOnsale(user, q, page_size, page_no, 2);
            List<Good> goodList = result.getItems();
            modelAndView.addObject("result_total", result.getTotal());
            modelAndView.addObject("goods", goodList);
        }
        return modelAndView;
    }
}
