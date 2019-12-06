package com.icoding.interceptor;

import com.icoding.utils.HttpUtil;
import com.icoding.utils.SSOClientUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

public class SsoClientInterceptor implements HandlerInterceptor {

    //返回true，代表放行，返回false，代表被拦截
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1. 是否存在回话 (isLogin)
        HttpSession session = request.getSession();
        Boolean isLogin = (Boolean) session.getAttribute("isLogin");

        if (isLogin!=null && isLogin){ //可以放行
            return true;
        }

        // 客户端拦截器中，判断token参数
        String token = request.getParameter("token");
        if (!StringUtils.isEmpty(token)){ //
            System.out.println("检测token信息"); //防止token信息，被伪造，我们还需要去服务器验证！
            //拿着这个token再一次去服务器请求验证,服务器验证方法
            String httpURL = SSOClientUtil.SERVER_URL_PREFIX + "/verify";
            //需要验证参数
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("token",token);
            params.put("clientUrl",SSOClientUtil.getClientLogOutUrl());
            params.put("jsessionid",session.getId());
            try {
                String isVerify = HttpUtil.sendHttpRequest(httpURL, params);//发起一个请求~
                if ("true".equals(isVerify)){ //说明这个 token 是统一认证中心产生的！
                    System.out.println("验证通过");
                    session.setAttribute("isLogin",true);
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        //不能放行，没有会话信息，登录
        // http://www.sso.com:8001/checkLogin?redirectUrl=http://www.tm.com:8002/index
        SSOClientUtil.redirectToSSOURL(request,response);
        return false;
    }
}
