package com.testweather.jpzhang.weather.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.testweather.jpzhang.weather.R;
import com.testweather.jpzhang.weather.db.City;
import com.testweather.jpzhang.weather.db.County;
import com.testweather.jpzhang.weather.db.Province;
import com.testweather.jpzhang.weather.util.HttpUtil;
import com.testweather.jpzhang.weather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

//用于遍历省市区县的碎片
public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    //进度等待框
    private ProgressDialog progressDialog;
    //choose_area视图上的控件
    private TextView tittleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    //当前数据的列表
    private List<String> dataList = new ArrayList<>();
    //省列表
    private List<Province> provinceList;
    //城市列表
    private List<City> cityList;
    //区县列表
    private List<County> countyList;
    //选中的省
    private Province selectedProvince;
    //选中的城市
    private City selectedCity;
    //当前选中的级别
    private int currentLevel;
    //重写初始化加载布局方法，@Nullable表示可以为null
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);
        tittleText = (TextView)view.findViewById(R.id.tittleText);
        backButton = (Button)view.findViewById(R.id.backBtn);
        listView = (ListView)view.findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        return view;
    }
    //在碎片的生命周期中加载完onCreateView后会加载onActivityCreated方法，所以这里在这里重写方法
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //listview的单个item点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //判断如果当前选的是省，则把对应的省的信息取出来，进入下一步选择城市的页面
                if(currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);
                    queryCities();
                //判断如果当前选的是城市，则把对应的城市信息取出来，进入下一步选择区县页面
                }else if (currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(position);
                    queryCounties();
                }
            }
        });
        //返回按钮的点击事件
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断如果当前是选择区县页面，返回到选择城市页面
                if(currentLevel == LEVEL_COUNTY){
                    queryCities();
                //判断如果当前是选择城市页面，返回到选择省的页面
                }else if (currentLevel == LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
        queryProvinces();
    }
    //查询全国所有的省，优先从数据库查询，如果没有数据再到服务器上查询
    private void queryProvinces(){
        //设置页面标题为中国，因为当前是最开始的页面，所以把返回按钮隐藏
        tittleText.setText("中国");
        backButton.setVisibility(View.GONE);
        //通过DataSupport类去数据库查Province表的数据
        provinceList = DataSupport.findAll(Province.class);
        //如果省数据列表有数据，先清空当前列表，遍历省数据列表添加到当前列表，再刷新适配器，默认选中第一条，设置当前级别为省
        if(provinceList.size()>0){
            dataList.clear();
            for(Province province:provinceList){
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        }else{
            String address = "http://guolin.tech/api/china";//作者在服务器上存放的数据
            queryFromServer(address,"province");
        }
    }
    //查询选中的省中所有的城市，优先从数据库查询，如果没有数据再到服务器上查询
    private void queryCities(){
        //设置页面标题为当前选中的省，把返回按钮显示出来
        tittleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where("provinceid = ?",String.valueOf(selectedProvince.getId())).find(City.class);
        if(cityList.size()>0){
            //先清除当前列表，再加载城市列表
            dataList.clear();
            for(City city:cityList){
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        }else{
            int provinceCode = selectedProvince.getProvinceCode();
            String address = "http://guolin.tech/api/china/"+provinceCode;//作者在服务器上存放的数据
            queryFromServer(address,"city");
        }
    }
    //查询选中的城市中所有的区县，优先从数据库查询，如果没有数据再到服务器上查询
    private void queryCounties(){
        //设置页面标题为当前选中的城市，把返回按钮显示出来
        tittleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where("cityid = ?",String.valueOf(selectedCity.getId())).find(County.class);
        if(countyList.size()>0){
            dataList.clear();
            for(County county:countyList){
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        }else{
            int provinceCode = selectedProvince.getProvinceCode();
            int cityCode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/"+provinceCode+"/"+cityCode;//作者在服务器上存放的数据
            queryFromServer(address,"county");
        }
    }
    //根据传入的地址和类型从服务器上查询省市区县数据
    private void queryFromServer(final String address, final String type){
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            //访问服务器成功调用onResponse
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //获取响应到的json数据，默认结果失败
                String responseText = response.body().string();
                boolean result = false;
                //判断查询的是省的数据还是城市的数据还是区县的数据，调用对应的方法处理json数据，处理成功返回result = true
                if("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);
                }else if ("city".equals(type)){
                    result = Utility.handleCitiesResponse(responseText,selectedProvince.getId());
                }else if("county".equals(type)){
                    result = Utility.handleCountiesResponse(responseText,selectedCity.getId());
                }
                //解析json数据成功
                if(result){
                    //从子线程切换到主线程
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if("province".equals(type)){
                                queryProvinces();
                            }else if ("city".equals(type)){
                                queryCities();
                            }else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
            //访问服务器失败调用onFailure
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
    //显示进度对话框
    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载。。。");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    //关闭进度对话框
    private void closeProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }
}

















