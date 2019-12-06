package com.icoding.controller;

import com.icoding.pojo.ClientInfo;
import com.icoding.db.MockDB;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class SsoServerController {

    @RequestMapping("/index")
    public String index(){
        return "login";
    }

    @RequestMapping("/login")
    public String login(String username,String password,HttpSession session,String redirectUrl,Model model){
        System.out.println("Debug:username=>"+username+"-- password=>"+password);

        //验证数据库数据
        if ("admin".equals(username) && "123456".equals(password)){
            //1. 校验成功
            //2. 创建token信息： 保证唯一
            String token = UUID.randomUUID().toString();
            System.out.println("token生成功=>"+token);
            //3. 创建一个全局回话，把token放到session中
            session.setAttribute("token",token);
            //4. token 保存！ 数据结构Set
            MockDB.T_TOKEN.add(token);
            //5. 登陆成功了，需要吧这个token返回给客户端
            model.addAttribute("token",token);
            return "redirect:" + redirectUrl;
        }

        //密码不正确，继续停留在登陆页面
        System.out.println("用户名或密码不正确");
        model.addAttribute("redirectUrl",redirectUrl);
        return "login";
    }


    //
    @RequestMapping("/checkLogin")
    public String checkLogin(String redirectUrl, HttpSession session, Model model){
        //1.服务区验证你这个用户的时候，会发送token，没有
        String token = (String) session.getAttribute("token");

        if (StringUtils.isEmpty(token)){
            //没有全局会话，跳到登录~ 连接或者页面已经发送了变化，我们继续把这个参数带着
            model.addAttribute("redirectUrl",redirectUrl);
            return "login";
        }else {
            //存在全局回话，token~ ，回到来时的地方 ， 我忘记了！存的是一个 token
            model.addAttribute("token",token);
            return "redirect:"+redirectUrl;
        }
    }

    @RequestMapping("/verify")
    @ResponseBody
    public String verify(String token,String clientUrl,String jsessionid){
        //判断这个token是不是服务器产生的！
        if (MockDB.T_TOKEN.contains(token)){
            System.out.println("服务器端token信息验证通过"); //登陆成功！需要用户的登出和对应的session！

            List<ClientInfo> clientInfoList = MockDB.T_CLIENT_INFO.get(token);

            if (clientInfoList==null){ // 第一次为空的问题
                clientInfoList = new ArrayList<ClientInfo>();
                MockDB.T_CLIENT_INFO.put(token,clientInfoList);
            }

            //将信息放入
            ClientInfo clientInfo = new ClientInfo();
            clientInfo.setClientUrl(clientUrl);
            clientInfo.setJsessionid(jsessionid);
            clientInfoList.add(clientInfo);

            return "true";
        }
        return "false";
    }

    @RequestMapping("/logOut")
    public String logOut(HttpSession session){
        session.invalidate();
        // session销毁，  手动，自动，监听器，监听session销毁时间
        return "login";
    }

}
