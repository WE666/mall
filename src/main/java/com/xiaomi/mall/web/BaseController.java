package com.xiaomi.mall.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author lcyang
 * @Date 2018/11/11 10:44
 * @Description
 */
@Controller
public class BaseController {
    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/register")
    public String register(){
        return "register";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }
}
