package com.testweather.jpzhang.weather.util;

import android.text.TextUtils;

import com.testweather.jpzhang.weather.db.City;
import com.testweather.jpzhang.weather.db.County;
import com.testweather.jpzhang.weather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//因为发送请求到服务器后，服务器响应回来的省市区县数据都是json格式，所以需要另一个解析处理json格式的类
public class Utility {
    /*
    * 解析和处理服务器返回的省级数据
    * */
    public static boolean handleProvinceResponse(String response){
        //判断服务器响应的数据是否为空，不为空进行一个json解析处理
        if(!TextUtils.isEmpty(response)){
            try {
                //实例化一个json数组对象JSONArray[{"a":"1","b":"2"},{"c":"3","d":"4"}]
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0;i < allProvinces.length();i++){
                    //用json格式对象JSONObject获取遍历的JSONArray数组,JSONObject{"a":"1","b":"2"}
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    //实例化一条省的数据，取出服务器响应的数据并赋值
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    //调用save()方式将数据存储到数据库里
                    province.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    /*
     * 解析和处理服务器返回的市级数据
     * */
    public static boolean handleCitiesResponse(String response,int provinceId){
        //判断服务器响应的数据是否为空，不为空进行一个json解析处理
        if(!TextUtils.isEmpty(response)){
            try {
                //实例化一个json数组对象JSONArray[{"a":"1","b":"2"},{"c":"3","d":"4"}]
                JSONArray allCitys = new JSONArray(response);
                for (int i = 0;i < allCitys.length();i++){
                    //用json格式对象JSONObject获取遍历的JSONArray数组,JSONObject{"a":"1","b":"2"}
                    JSONObject cityObject = allCitys.getJSONObject(i);
                    //实例化一条市的数据，取出服务器响应的数据并赋值
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    //调用save()方式将数据存储到数据库里
                    city.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }
    /*
     * 解析和处理服务器返回的区县级数据
     * */
    public static boolean handleCountiesResponse(String response,int cityId){
        //判断服务器响应的数据是否为空，不为空进行一个json解析处理
        if(!TextUtils.isEmpty(response)){
            try {
                //实例化一个json数组对象JSONArray[{"a":"1","b":"2"},{"c":"3","d":"4"}]
                JSONArray allCountys = new JSONArray(response);
                for (int i = 0;i < allCountys.length();i++){
                    //用json格式对象JSONObject获取遍历的JSONArray数组,JSONObject{"a":"1","b":"2"}
                    JSONObject countyObject = allCountys.getJSONObject(i);
                    //实例化一条区县的数据，取出服务器响应的数据并赋值
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    //调用save()方式将数据存储到数据库里
                    county.save();
                }
                return true;
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return false;
    }

}
