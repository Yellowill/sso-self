package com.icoding.db;

import com.icoding.pojo.ClientInfo;

import java.util.*;

public class MockDB {

    //1. 保存token信息
    public static Set<String> T_TOKEN = new HashSet<String>();

    //2. 保存用户的信息, 登出的地址，session:ClientInfo    Map<String,List<ClientInfo>>
    public static Map<String,List<ClientInfo>> T_CLIENT_INFO = new HashMap<String, List<ClientInfo>>();

}
