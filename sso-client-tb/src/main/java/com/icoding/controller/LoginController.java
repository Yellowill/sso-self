package com.icoding.controller;

import com.icoding.utils.SSOClientUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    //在客户端需要知道服务器的登出地址
    @RequestMapping("/taobao")
    public String index(Model model){
        model.addAttribute("getServerLogOutUrl", SSOClientUtil.getServerLogOutUrl());
        return "taobao";
    }

    @RequestMapping("/logOut")
    public void logOut(HttpSession session){
        session.invalidate();
    }
}
