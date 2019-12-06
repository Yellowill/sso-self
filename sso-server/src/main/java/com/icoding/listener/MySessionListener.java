package com.icoding.listener;

import com.icoding.db.MockDB;
import com.icoding.pojo.ClientInfo;
import com.icoding.utils.HttpUtil;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.List;

public class MySessionListener implements HttpSessionListener {

    public void sessionCreated(HttpSessionEvent se) {

    }
    //销毁的监听事件
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        String token = (String) session.getAttribute("token");
        //销毁数据库（缓存）中数据
        MockDB.T_TOKEN.remove(token);
        List<ClientInfo> clientInfoList = MockDB.T_CLIENT_INFO.remove(token);
        try {
            for (ClientInfo clientInfo : clientInfoList) {
                //通知销毁
                HttpUtil.sendHttpRequest(clientInfo.getClientUrl(),clientInfo.getJsessionid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
