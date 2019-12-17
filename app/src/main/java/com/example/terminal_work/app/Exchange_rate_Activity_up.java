package com.example.terminal_work.app;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.terminal_work.R;

import java.util.ArrayList;
import java.util.List;

public class Exchange_rate_Activity_up extends AppCompatActivity {

    List<String> datelist;//创建数据源

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_rate_page);//加载汇率的布局

        ListView exchange_rate_list = findViewById(R.id.exchange_rate_list);//找到xml中的listview

        datelist = new ArrayList<>();
        datelist.add("人民币CNY");datelist.add("欧元EUR");
        datelist.add("泰铢THB");datelist.add("澳元AUD");
        datelist.add("巴西雷亚尔BRL");datelist.add("加拿大元CAD");
        datelist.add("丹麦克朗DKK");datelist.add("英镑GBP");
        datelist.add("港币HKD");datelist.add("印度卢比INR");
        datelist.add("柬埔寨瑞尔KHR");datelist.add("澳门币MOP");
        datelist.add("新西兰元NZD");datelist.add("瑞典克朗SEK");
        datelist.add("美元USD");datelist.add("越南盾VND");

        Exchange_list_Adapter exchange_list_adapter = new Exchange_list_Adapter();//会自动调用声明类里的方法
        exchange_rate_list.setAdapter(exchange_list_adapter);

        exchange_rate_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //对listview的点击事件
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent[] intent=new Intent[16];//直接用一个Itent数组
                  switch(position){
                      case 0:
                          setResult(0,intent[0]);
                          finish();
                      case 1:
                          setResult(1,intent[1]);
                          finish();
                      case 2:
                          setResult(2,intent[2]);
                          finish();
                      case 3:
                          setResult(3,intent[3]);
                          finish();
                      case 4:
                          setResult(4,intent[4]);
                          finish();
                      case 5:
                          setResult(5,intent[5]);
                          finish();
                      case 6:
                          setResult(6,intent[6]);
                          finish();
                      case 7:
                          setResult(7,intent[7]);
                          finish();
                      case 8:
                          setResult(8,intent[8]);
                          finish();
                      case 9:
                          setResult(9,intent[9]);
                          finish();
                      case 10:
                          setResult(10,intent[10]);
                          finish();
                      case 11:
                          setResult(11,intent[11]);
                          finish();
                      case 12:
                          setResult(12,intent[12]);
                          finish();
                      case 13:
                          setResult(13,intent[13]);
                          finish();
                      case 14:
                          setResult(14,intent[14]);
                          finish();
                      case 15:
                          setResult(15,intent[15]);
                          finish();

                  }
            }
        });
    }


    class Exchange_list_Adapter extends BaseAdapter {//先创建一个类去继承BaseAdapter
        @Override
        public int getCount() {//用来返回有多少个数据需要显示
            return datelist.size();//我们的数据源有多少个时候，这个datelist就应该适配多少个itemview
        }
        @Override
        public Object getItem(int position) {//用来返回指定position的数据
            return datelist.get(position);
        }
        @Override
        public long getItemId(int position) {//通常返回position就行了
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {//在这个方法里将会添加所有生成view的逻辑
            //先根据xml文件布局生成一个view，也就是list里的layout
            View itemRootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.exchange_rate_item, null);//这样就生成了exchange_rate_item的view
            TextView tvName = itemRootView.findViewById(R.id.tv_name);//找到布局里显示姓名的控件
            tvName.setText(datelist.get(position));//用datelist对他进行赋值
            tvName.setTextSize(20);
            ImageView country_image=itemRootView.findViewById(R.id.country_image);//找到布局里显示图案的控件
            switch (datelist.get(position)){
                case "人民币CNY":
                    country_image.setBackgroundResource(R.drawable.cny);//人民币
                    break;
                case "港币HKD":
                    country_image.setBackgroundResource(R.drawable.hkd);//港币
                    break;
                case "欧元EUR":
                    country_image.setBackgroundResource(R.drawable.eur);//欧元
                    break;
                case "澳元AUD":
                    country_image.setBackgroundResource(R.drawable.aud);//澳元
                    break;
                case "巴西雷亚尔BRL":
                    country_image.setBackgroundResource(R.drawable.brl);//巴西雷亚尔
                    break;
                case "加拿大元CAD":
                    country_image.setBackgroundResource(R.drawable.cad);//加拿大元
                    break;
                case "丹麦克朗DKK":
                    country_image.setBackgroundResource(R.drawable.dkk);//丹麦克朗
                    break;
                case "英镑GBP":
                    country_image.setBackgroundResource(R.drawable.gbp);//英镑
                    break;
                case "印度卢比INR":
                    country_image.setBackgroundResource(R.drawable.inr);//印度卢比
                    break;
                case "柬埔寨瑞尔KHR":
                    country_image.setBackgroundResource(R.drawable.khr);//柬埔寨瑞尔
                    break;
                case "澳门币MOP":
                    country_image.setBackgroundResource(R.drawable.mop);//澳门币
                    break;
                case "新西兰元NZD":
                    country_image.setBackgroundResource(R.drawable.nzd);//新西兰元
                    break;
                case "瑞典克朗SEK":
                    country_image.setBackgroundResource(R.drawable.sek);//瑞典克朗
                    break;
                case "泰铢THB":
                    country_image.setBackgroundResource(R.drawable.thb);//泰铢
                    break;
                case "美元USD":
                    country_image.setBackgroundResource(R.drawable.usd);//美元
                    break;
                case "越南盾VND":
                    country_image.setBackgroundResource(R.drawable.vnd);//越南盾
                    break;
            }
            return itemRootView;
        }

    }
}

