package com.testweather.jpzhang.weather.db;

import org.litepal.crud.DataSupport;
//城市表
public class City extends DataSupport {
    //主键
    private int id;
    //城市名称
    private String cityName;
    //城市代码编号
    private int cityCode;
    //省的id
    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
