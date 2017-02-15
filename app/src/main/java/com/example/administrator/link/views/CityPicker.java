package com.example.administrator.link.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.administrator.link.R;
import com.example.administrator.link.data.Cityinfo;
import com.example.administrator.link.utils.CitycodeUtil;
import com.example.administrator.link.utils.FileUtil;
import com.example.administrator.link.utils.JSONPaser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/2/13.
 */
public class CityPicker extends LinearLayout {
    private ScrollerNumberPicker provincePicker;
    private ScrollerNumberPicker cityPicker;
    private ScrollerNumberPicker countyPicker;
    private OnSelectingListhener onSelectingListhener;
    private static final int REFRESH_VIEW=0x001;
    private int tempProvinceIndex=-1;
    private int tempCityIndex=-1;
    private int tempCounyIndex=-1;
    private List<Cityinfo> provinces_list=new ArrayList<Cityinfo>();
    private HashMap<String,List<Cityinfo>> cityMap=new HashMap<>();
    private HashMap<String, List<Cityinfo>> counyMap=new HashMap<>();
    private static ArrayList<String> province_list_code=new ArrayList<>();
    private static ArrayList<String> city_list_code=new ArrayList<>();
    private static ArrayList<String> couny_list_code=new ArrayList<>();
    private CitycodeUtil citycodeUtil;
    private String city_code_String;
    private String city_string;

    public CityPicker(Context context) {
        super(context);
        getAddressInfo(context);
    }

    public CityPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAddressInfo(context);
    }

    public CityPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAddressInfo(context);
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case REFRESH_VIEW:
                    if(onSelectingListhener!=null){
                        onSelectingListhener.selected(true);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private void getAddressInfo(Context context){
        JSONPaser paser=new JSONPaser();
        String area_str= FileUtil.readAssets(context,"area.json");
        Log.e("area_str",area_str);
            provinces_list = paser.getJSONParserResult(area_str, "area0");
            cityMap = paser.getJSONPaserResultArray(area_str, "area1");
            counyMap = paser.getJSONPaserResultArray(area_str, "area2");

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.click_picker,this);
        citycodeUtil=CitycodeUtil.getInstance();
        provincePicker= (ScrollerNumberPicker) findViewById(R.id.province);
        cityPicker= (ScrollerNumberPicker) findViewById(R.id.city);
        countyPicker= (ScrollerNumberPicker) findViewById(R.id.couny);
        provincePicker.setData(citycodeUtil.getProvince(provinces_list));
        provincePicker.setDefault(1);
        cityPicker.setData(citycodeUtil.getCity(cityMap,citycodeUtil.getProvince_list_code().get(1)));
        cityPicker.setDefault(1);
        countyPicker.setData(citycodeUtil.getCity(counyMap,citycodeUtil.getCity_list_code().get(1)));
        countyPicker.setDefault(1);
        provincePicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                if(text.equals("") || text==null){
                    return;
                }
                if (tempProvinceIndex!=id){
                    String selectDay=cityPicker.getSelectedText();
                    if (selectDay==null || selectDay.equals("")){
                        return;
                    }
                    String selectMonth=countyPicker.getSelectedText();
                    if (selectMonth==null || selectMonth.equals(""))
                    {
                        return;
                    }
                    cityPicker.setData(citycodeUtil.getCity(cityMap,citycodeUtil.getProvince_list_code().get(id)));
                    cityPicker.setDefault(1);
                     countyPicker.setData(citycodeUtil.getCouny(counyMap,citycodeUtil.getCity_list_code().get(1)));
                    countyPicker.setDefault(1);
                    int lastDay=Integer.valueOf(provincePicker.getListSize());
                    if(id>lastDay){
                        provincePicker.setDefault(lastDay-1);
                    }
                    tempProvinceIndex=id;
                    sendMessage();
                }
            }

            @Override
            public void selecting(int id, String text) {

            }
        });
        cityPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                if (text.equals("")|| text==null){
                    return;
                }
                if (tempCityIndex!=id){
                    String selectDay=provincePicker.getSelectedText();
                    if (selectDay.equals("")||selectDay==null){
                        return;
                    }
                    String selectMoth=countyPicker.getSelectedText();
                    if (selectMoth.equals("")||selectMoth==null){
                        return;
                    }
                    countyPicker.setData(citycodeUtil.getCouny(counyMap,citycodeUtil.getCity_list_code().get(id)));
                    countyPicker.setDefault(1);
                    int lastDay=Integer.valueOf(cityPicker.getListSize());
                    if (lastDay<id){
                        cityPicker.setDefault(lastDay-1);
                    }
                    tempCityIndex=id;
                    sendMessage();
                }

            }

            @Override
            public void selecting(int id, String text) {

            }
        });
        countyPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {
            @Override
            public void endSelect(int id, String text) {
                if (text.equals("") || text == null)
                    return;
                if (tempCounyIndex != id) {
                    String selectDay = provincePicker.getSelectedText();
                    if (selectDay == null || selectDay.equals(""))
                        return;
                    String selectMonth = cityPicker.getSelectedText();
                    if (selectMonth == null || selectMonth.equals(""))
                        return;
                    // 城市数组
                    city_string = citycodeUtil.getCouny_list_code()
                            .get(id);
                    int lastDay = Integer.valueOf(countyPicker.getListSize());
                    if (id > lastDay) {
                        countyPicker.setDefault(lastDay - 1);
                    }
                }
                tempCounyIndex = id;
                sendMessage();
            }

            @Override
            public void selecting(int id, String text) {

            }
        });
    }


    private void sendMessage() {
        Message msg=new Message();
        msg.what=REFRESH_VIEW;
        handler.handleMessage(msg);
    }

    public void setOnSelectingListhener(OnSelectingListhener onSelectingListhener){
        this.onSelectingListhener=onSelectingListhener;
    }
    public interface OnSelectingListhener{
        void selected(boolean selected);
    }
}
