package com.midream.sheep.api.http;

import com.midream.sheep.swcj.pojo.ExecuteValue;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPTool {

    public static HttpURLConnection getHttpURLConnection(ExecuteValue executeValue) throws IOException {
        URL url = new URL(executeValue.getUrl());
        //得到连接对象
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        //设置请求类型
        con.setRequestMethod(executeValue.getType().getValue());
        //设置请求需要返回的数据类型和字符集类型
        con.setRequestProperty("Content-Type", "application/json;charset=GBK");
        con.setRequestProperty("cookie", executeValue.getCookies());
        con.setRequestProperty("user-agent", executeValue.getUserAge());
        //set the request cookie
        con.setRequestProperty("cookie", executeValue.getCookies());
        //set the request timeout
        con.setConnectTimeout(Integer.parseInt(executeValue.getTimeout()));
        //set the request method
        con.setRequestProperty("method", executeValue.getType().getValue());
        //set the values
        con.setRequestProperty("connection", "Keep-Alive");
        //允许写出
        con.setDoOutput(true);
        //允许读入
        con.setDoInput(true);
        //不使用缓存
        con.setUseCaches(false);
        return con;
    }
}
