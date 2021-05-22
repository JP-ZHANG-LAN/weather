package com.testweather.jpzhang.weather.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

//网络连接工具类
public class HttpUtil {
    //发送请求到服务器，传入服务器地址address，然后通过request获取连接，注册一个callback回调来处理服务器响应
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
