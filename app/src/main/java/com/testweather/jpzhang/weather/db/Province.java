package com.testweather.jpzhang.weather.db;

import org.litepal.crud.DataSupport;
//省名表
public class Province extends DataSupport {
    //主键
    private int id;
    //省名
    private String provinceName;
    //省的代码编号
    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }
}
