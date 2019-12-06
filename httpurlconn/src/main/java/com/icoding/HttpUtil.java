package com.icoding;

import org.springframework.util.StreamUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

public class HttpUtil {
    public static String sendHttpRequest(String httpUrl, Map<String,String> params)  throws Exception{
        //1. 定义需要访问的接口地址
        URL url = new URL(httpUrl);
        //2. 打开连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        //3. 请求方式
        connection.setRequestMethod("POST");
        //4. 输出参数
        connection.setDoOutput(true);
        //5. 拼接参数
        //Url:https://way.jd.com/he/freeweather?city=beijing&appkey=06642046425c68a351817b5b020b591f

        if (params!=null && params.size()>0){
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
            }
            //去掉最前面的 &
            //6、写出参数,可能会存在乱码
            connection.getOutputStream().write(sb.substring(1).toString().getBytes("UTF-8"));
        }

        //7. 发起请求
        connection.connect();
        //8. 接受服务器响应的东西
        String response = StreamUtils.copyToString(connection.getInputStream(), Charset.forName("UTF-8"));
        return response;
    }
}
