package com.testweather.jpzhang.weather.db;

import org.litepal.crud.DataSupport;
//区县表
public class County extends DataSupport{
    //主键
    private int id;
    //区县名称
    private String countyName;
    //区县对应的天气
    private String weatherId;
    //城市id
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
