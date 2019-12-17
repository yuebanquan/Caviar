package com.example.terminal_work.app;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.terminal_work.R;
import com.example.terminal_work.litepal.AssetAccount;
import com.example.terminal_work.litepal.Wish;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.ArrayList;
import java.util.List;


public class Show_Wish_Activity extends AppCompatActivity {

    private LinearLayout show_linearlayout;

    String get_W_name;//获取心愿名称
    double get_W_money;//获取心愿金额
    int get_W_month;//获取心愿实现月份

    int[] wish_item_pic;//心愿项背景图
    private ArrayList<LinearLayout> Linear_list = new ArrayList<LinearLayout>();

    TextView wish_name;
    SwipeMenuLayout swipmenu;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_wish_page);//加载显示心愿的布局

        show_linearlayout = findViewById(R.id.show_linearlayout);//找到xml里的总布局

        wish_item_pic = new int[]{R.drawable.wish_item_pic1, R.drawable.wish_item_pic2, R.drawable.wish_item_pic3
                , R.drawable.wish_item_pic4, R.drawable.wish_item_pic5};

        /**
         * 异步取数据库中的数据
         */
        LitePal.findAllAsync(Wish.class).listen(new FindMultiCallback<Wish>() {
            @Override
            public void onFinish(List<Wish> list) {
                list = LitePal.findAll(Wish.class);//找到所有数据
                create_wish_item(list);
            }
        });
    }

    /**
     * 动态生成心愿列表项
     */
    private void create_wish_item(List<Wish> list) {

        for (int size = 0;size < list.size(); size++) {

            get_W_name = list.get(size).getW_name();
            get_W_money = list.get(size).getW_money();
            get_W_month = list.get(size).getW_month();


            //侧滑菜单需要布局编写（第一个子view放显示部件，第二个子view放侧滑部件）
            swipmenu = new SwipeMenuLayout(this);
            LinearLayout.LayoutParams swipmenu_Params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 220);
            swipmenu_Params.setMargins(0, 20, 0, 0);
            swipmenu.setLayoutParams(swipmenu_Params);
            //侧滑菜单需要布局编写完成

            //创建第二层布局
            LinearLayout second = new LinearLayout(this);
            Linear_list.add(second);//先将每次创建的第二层布局加入到链表中
            //随机取一个背景
            int pic = (int) (Math.random() * wish_item_pic.length);
            second.setBackgroundResource(wish_item_pic[pic]);
            LinearLayout.LayoutParams second_Params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 220);
            second_Params.setMargins(0, 40, 0, 0);
            second.setLayoutParams(second_Params);
            second.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //点击跳转到新的页面，根据内容动态加载不同内容
                    Intent intent = new Intent(Show_Wish_Activity.this, wish_details.class);

                    // intent.putExtra("locaotion",);//第一个参数可以理解为变量名，第二个为传递内容
                    //要确定是第几个第二层布局传入,所以第二层布局要用个链表或者数组存

                    startActivity(intent);
                }
            });
            //第二层布局编写完毕

            //第三层布局（垂直布局，用来存放心愿名和金额）
            LinearLayout third = new LinearLayout(Show_Wish_Activity.this);
            third.setOrientation(LinearLayout.VERTICAL);//设为垂直
            LinearLayout.LayoutParams third_Params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            third_Params.setMargins(50, 0, 0, 0);
            third.setLayoutParams(third_Params);
            //第三层布局编写完毕

            //心愿名字编写
            wish_name=new TextView(this);
            wish_name.setText(get_W_name);
            wish_name.setTextSize(22);
            wish_name.setTextColor(Color.WHITE);
            wish_name.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);//设置字体加粗
            RelativeLayout.LayoutParams wish_name_layout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            wish_name_layout.setMargins(0, 50, 0, 0);
            wish_name.setLayoutParams(wish_name_layout);
            //名字编写完毕


            //金额编写
            TextView wish_money = new TextView(this);
            wish_money.setText("期望金额" + get_W_money);
            wish_money.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);//设置字体加粗
            wish_money.setTextSize(15);
            wish_money.setTextColor(Color.WHITE);
            RelativeLayout.LayoutParams wish_money_layout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            wish_money_layout.setMargins(0, 0, 0, 0);
            wish_money.setLayoutParams(wish_money_layout);

            //金额编写完毕

            //期望实现月份编写
            TextView wish_month = new TextView(this);
            wish_month.setText("期望" + get_W_month + "个月实现");
            wish_month.setTextSize(16);
            wish_month.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);//设置字体加粗
            wish_month.setTextColor(Color.WHITE);
            RelativeLayout.LayoutParams wish_month_layout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            wish_month_layout.setMargins(350, 85, 0, 4);
            wish_month.setLayoutParams(wish_month_layout);
            //期望实现月份编写完毕

            //分割线编写
            View view = new View(this);
            view.setBackgroundColor(getResources().getColor(R.color.color1));
            LinearLayout.LayoutParams view_Params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 3);
            view_Params.setMargins(35, 0, 35, 0);
            view.setLayoutParams(view_Params);

            //侧滑删除按钮编写
            Button delete = new Button(this);
            delete.setText("删除");
            delete.setTextColor(getResources().getColor(R.color.color4));
            delete.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);//字体加粗
            delete.setBackgroundColor(getResources().getColor(R.color.color8));
            LinearLayout.LayoutParams delete_Params = new LinearLayout.LayoutParams(280, ViewGroup.LayoutParams.MATCH_PARENT);
            delete.setLayoutParams(delete_Params);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //在这里编写删除事件,用delete[size]是为了用size来标识是第几个删除按钮
                    //将和心愿名字对应的id取出来,心愿名字又用size指定，和button的size一致！
                    String w_n=wish_name.getText().toString();
                    LitePal.deleteAll(Wish.class,"W_name=?",w_n);//数据库中删除指定位置的心愿
                    swipmenu.removeAllViews();
                }
            });

            third.addView(wish_name);
            third.addView(wish_money);
            second.addView(third);//将第三层布局加入的二层中
            second.addView(wish_month);
            swipmenu.addView(second);//将第二层布局加入到侧滑菜单布局中当第一个子view
            swipmenu.addView(delete);//将侧滑显示控件加入到侧滑菜单中当第二个字view
            show_linearlayout.addView(swipmenu);//将侧滑菜单布局加入到总布局中
            show_linearlayout.addView(view);//加入分割线
        }

    }

}
