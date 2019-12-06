package com.icoding;

import java.util.HashMap;
import java.util.Map;

public class HttpTest {
    public static void main(String[] args) throws Exception {
        //Url:https://way.jd.com/he/freeweather?city=beijing&appkey=06642046425c68a351817b5b020b591f
        Map<String, String> params = new HashMap<String, String>();
        params.put("city","beijing");
        params.put("appkey","06642046425c68a351817b5b020b591f");

        System.out.println(HttpUtil.sendHttpRequest("https://way.jd.com/he/freeweather", params));
    }
}