package com.example.terminal_work.app;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.terminal_work.R;
import com.example.terminal_work.litepal.AssetAccount;
import com.example.terminal_work.litepal.Classify;
import com.example.terminal_work.litepal.Tally;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.math.BigDecimal;
import java.util.List;

public class BillFragment extends Fragment {
    /**
     * extView bill_select  声明账单页面里筛选TextView键
     * DrawerLayout bill_drawerLayout 声明账单页面的根布局为DrawerLayout，用于侧滑菜单的实现
     * TextView bill_time 声明账单页面的选择日期控件
     * Button reset 重置按钮
     * RadioGroup rdg_one 筛选侧滑栏的最上面的选择全部收入或支出的radiogroup，用于重置功能的清除
     * CheckBox ckb1,ckb2,ckb3,ckb4,ckb5,ckb6,ckb7,ckb8,ckb9,ckb10,
     * ckb11,ckb12,ckb13,ckb14,ckb15,ckb16,ckb17,ckb18,ckb19,ckb20,ckb21   筛选侧滑栏里的所有复选框
     * int[] kind_icon  记一笔的类别图片源
     * LinearLayout layout_one 将动态添加账单item加入到xml里的根布局的根布局
     */
    public static final int BILL_DIALOG_TYPE_DATE = 1;//确定汇率计算器的带参返回子活动标志
    private TextView bill_select;//声明账单页面里筛选TextView键
    private DrawerLayout bill_drawerLayout;//声明账单页面的根布局
    private TextView bill_time;//声明账单页面的时间选择控件
    private Button reset;//重置按钮
    private RadioGroup rdg_one;
    private CheckBox ckb1, ckb2, ckb3, ckb4, ckb5, ckb6, ckb7, ckb8, ckb9, ckb10,
            ckb11, ckb12, ckb13, ckb14, ckb15, ckb16, ckb17, ckb18, ckb19, ckb20, ckb21;
    //记一笔的类别图片源
    private int[] kind_icon = {R.drawable.ic_local_dining_black_24dp2, R.drawable.ic_airplanemode_active_black_24dp2,
            R.drawable.ic_shopping_cart_black_24dp2, R.drawable.ic_subway_black_24dp2,
            R.drawable.ic_phone_in_talk_black_24dp2
            , R.drawable.ic_local_hospital_black_24dp2, R.drawable.ic_home_black_24dp_big2,
            R.drawable.ic_child_care_black_24dp2, R.drawable.ic_school_black_24dp2
            , R.drawable.ic_rowing_black_24dp2, R.drawable.ic_pets_black_24dp2,
            R.drawable.ic_headset_mic_black_24dp2};
    //    //记一笔的类别图片源对应的名称
//    private final String[] kind_name = {"餐饮", "旅行", "购物", "交通", "通讯", "医疗", "住房", "育儿", "文教", "娱乐", "宠物", "生活"};
    private LinearLayout layout_one;//xml里的动态加入的主布局
    private static final String TAG = "BillFragment";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflater使将xml布局文件转换为视图的一个类,container表示在container里面显示这个视图
        View view = inflater.inflate(R.layout.bill_page, container, false);


        /**
         * 初始化函数
         */
        //初始化所有声明的变量控件
        layout_one = view.findViewById(R.id.layout_one);
        bill_select = view.findViewById(R.id.bill_select);//找到账单页面里筛选TextView键
        bill_drawerLayout = view.findViewById(R.id.bill_drawerlayout);//找到账单页面的drawerlayout的根布局
        bill_time = view.findViewById(R.id.time_selected);//找到时间选择的控件
        rdg_one = view.findViewById(R.id.rdg_one);
        ckb1 = view.findViewById(R.id.ckb_one);
        ckb2 = view.findViewById(R.id.ckb_two);
        ckb3 = view.findViewById(R.id.ckb_three);
        ckb4 = view.findViewById(R.id.ckb_four);
        ckb5 = view.findViewById(R.id.ckb_five);
        ckb6 = view.findViewById(R.id.ckb_six);
        ckb7 = view.findViewById(R.id.ckb_seven);
        ckb8 = view.findViewById(R.id.ckb_eight);
        ckb9 = view.findViewById(R.id.ckb_nine);
        ckb10 = view.findViewById(R.id.ckb_ten);
        ckb11 = view.findViewById(R.id.ckb_eleven);
        ckb12 = view.findViewById(R.id.ckb_twelve);
        ckb13 = view.findViewById(R.id.ckb_thirteen);
        ckb14 = view.findViewById(R.id.ckb_fourteen);
        ckb15 = view.findViewById(R.id.ckb_fifteen);
        ckb16 = view.findViewById(R.id.ckb_sixteen);
        ckb17 = view.findViewById(R.id.ckb_seventeen);
        ckb18 = view.findViewById(R.id.ckb_eighteen);
        ckb19 = view.findViewById(R.id.ckb_nineteen);
        ckb20 = view.findViewById(R.id.ckb_twenty);
        ckb21 = view.findViewById(R.id.ckb_twenty_one);
        reset = view.findViewById(R.id.reset);//找到重置按钮

        //监听事件的功能封装,所有的监听都放在里面
        all_Listene_BillFragment();


        /**
         * 异步取数据，用于动态生成账单里的项
         */
        LitePal.findAllAsync(Tally.class).listen(new FindMultiCallback<Tally>() {
            @Override
            public void onFinish(List<Tally> list) {
                list = LitePal.findAll(Tally.class, true);//找到所有数据
                add_pay(list);

            }
        });

        return view;//返回具体的布局
    }/**
     onCreate结束
     */


    /**
     * 监听类函数
     */
    private void all_Listene_BillFragment() {
        //1:筛选按钮点击事件：点击打开筛选侧滑菜单
        bill_select.setOnClickListener(new View.OnClickListener() {//编写点击显示侧滑菜单的事件
            @Override
            public void onClick(View v) {
                bill_drawerLayout.openDrawer(Gravity.END);//openDrawer()函数用来打开侧滑菜单,Gravity.START表示布局文件里设置的滑出方向
            }
        });
        //2:选择日期点击事件：点击打开日历弹窗
        //下面开始编写日历弹窗的内容
        bill_time.setOnClickListener(new View.OnClickListener() {//编写点击显示日期选择的事件
            @Override
            public void onClick(View v) {//这个点击事件应该弹出日期选择弹窗
                Calendar_dialog_Fragment calendar_dialog_fragment = new Calendar_dialog_Fragment().getInstance(BILL_DIALOG_TYPE_DATE);
                if (calendar_dialog_fragment != null) {
                    calendar_dialog_fragment.show(getFragmentManager(), "选择日期");
                }
            }
        });
        //3:重置按钮点击事件：点击重置筛选条件
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //清除选中状态
                rdg_one.clearCheck();
                ckb1.setChecked(false);
                ckb2.setChecked(false);
                ckb3.setChecked(false);
                ckb4.setChecked(false);
                ckb5.setChecked(false);
                ckb6.setChecked(false);
                ckb7.setChecked(false);
                ckb8.setChecked(false);
                ckb9.setChecked(false);
                ckb10.setChecked(false);
                ckb11.setChecked(false);
                ckb12.setChecked(false);
                ckb13.setChecked(false);
                ckb14.setChecked(false);
                ckb15.setChecked(false);
                ckb16.setChecked(false);
                ckb17.setChecked(false);
                ckb18.setChecked(false);
                ckb19.setChecked(false);
                ckb20.setChecked(false);
                ckb21.setChecked(false);
            }
        });
    }

    //动态加载记一笔支出的函数
    public void add_pay(List<Tally> list) {
        //编写动态生成记一笔的item
        for (int i = 0; i < list.size(); i++) {

            //先取出数据
            String string_classify_type = list.get(i).getT_classify().getC_type();//取出类别
            String string_asset_bankname = list.get(i).getT_assetAccount().getA_bankName();//取出银行名称

            double double_pay_money = list.get(i).getT_money();//取出支出金额
            String string_more_message = list.get(i).getT_comment();//取出备注信息
            if (TextUtils.isEmpty(string_more_message)) {//如果备注信息为空的话
                string_more_message = "无备注信息";
            }

            //查看取出的值
            Log.d(TAG, "add_pay: string_pay_money = " + double_pay_money);
            Log.d(TAG, "add_pay: string_more_message = " + string_more_message);
            Log.d(TAG, "add_pay: string_classify = " + string_classify_type);
            Log.d(TAG, "add_pay: string_asset_bankname = " + string_asset_bankname);

            //动态生成的总布局编写，（加上分割横线）
            LinearLayout layout_all = new LinearLayout(getContext());//新建
            LinearLayout.LayoutParams all_Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 160);
            all_Params.setMargins(0, 15, 0, 0);//设置边距
            layout_all.setOrientation(LinearLayout.VERTICAL);//设为垂直布局
            layout_all.setBackgroundColor(getResources().getColor(R.color.color4));//第动态总布局布局设置为白色背景
            layout_all.setLayoutParams(all_Params);//将上面的大小和边距属性赋给动态总布局
            //动态总布局编写完毕

            //编写第二层布局,横向的线性布局
            LinearLayout layout_two = new LinearLayout(getContext());//新建
            //第二层布局的大小等属性
            LinearLayout.LayoutParams two_Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 135);
            two_Params.setMargins(0, 15, 0, 0);//设置边距
            layout_two.setBackgroundColor(getResources().getColor(R.color.color4));//第二层布局设置为白色背景
            layout_two.setLayoutParams(two_Params);//将上面的大小和边距属性赋给第二层布局
            //第二层布局编写完毕

            //编写类别的ImageView
            ImageView kind_pic = new ImageView(getContext());//新建
            RelativeLayout.LayoutParams kind_pic_layout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            kind_pic_layout.setMargins(22, 0, 0, 0);//通过设置ImageView的边距位置达到居中效果
//            kind_pic.setBackgroundResource(kind_icon[0]);//给ImageView加上图片资源
            //给ImageView加上相应图片资源
            switch (string_classify_type) {
                case "餐饮":
                    kind_pic.setBackgroundResource(kind_icon[0]);
                    break;
                case "旅行":
                    kind_pic.setBackgroundResource(kind_icon[1]);
                    break;
                case "购物":
                    kind_pic.setBackgroundResource(kind_icon[2]);
                    break;
                case "交通":
                    kind_pic.setBackgroundResource(kind_icon[3]);
                    break;
                case "通讯":
                    kind_pic.setBackgroundResource(kind_icon[4]);
                    break;
                case "医疗":
                    kind_pic.setBackgroundResource(kind_icon[5]);
                    break;
                case "住房":
                    kind_pic.setBackgroundResource(kind_icon[6]);
                    break;
                case "育儿":
                    kind_pic.setBackgroundResource(kind_icon[7]);
                    break;
                case "文教":
                    kind_pic.setBackgroundResource(kind_icon[8]);
                    break;
                case "娱乐":
                    kind_pic.setBackgroundResource(kind_icon[9]);
                    break;
                case "宠物":
                    kind_pic.setBackgroundResource(kind_icon[10]);
                    break;
                case "生活":
                    kind_pic.setBackgroundResource(kind_icon[11]);
                    break;
            }


            kind_pic.setLayoutParams(kind_pic_layout);//将图片装入自己的布局
            layout_two.addView(kind_pic);//将imageview装入第二层布局中
            //ImageView编写完毕

            //第三层布局编写，第一个垂直线性布局
            LinearLayout layout_three = new LinearLayout(getContext());//新建
            LinearLayout.LayoutParams three_Params = new LinearLayout.LayoutParams(600, ViewGroup.LayoutParams.MATCH_PARENT);
            three_Params.setMargins(0, 5, 0, 0);//设置边距
            layout_three.setOrientation(LinearLayout.VERTICAL);//设为垂直布局
            layout_three.setBackgroundColor(getResources().getColor(R.color.color4));//第三层布局设置为白色背景
            layout_three.setLayoutParams(three_Params);//将上面的大小和边距属性赋给第三层布局
            //第三次布局编写完毕
            //第三次布局编写完毕

            //类别文字编写
            TextView t_one_up = new TextView(getContext());//新建
            RelativeLayout.LayoutParams t_one_up_layout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            t_one_up_layout.setMargins(30, 0, 0, 0);//通过设置ImageView的边距位置达到居中效果
            t_one_up.setText(string_classify_type);
            t_one_up.setTextSize(18);
            t_one_up.setTextColor(getResources().getColor(R.color.color3));//设置文字为黑色
            t_one_up.setLayoutParams(t_one_up_layout);//将类别文字装入自己的布局
            layout_three.addView(t_one_up);
            //类别文字编写完毕

            //备注信息文字编写
            TextView t_one_down = new TextView(getContext());//新建
            RelativeLayout.LayoutParams t_one_down_layout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            t_one_down_layout.setMargins(30, 0, 0, 0);//通过设置ImageView的边距位置达到居中效果
            t_one_down.setText(string_more_message);
            t_one_down.setTextSize(14);
            t_one_down.setTextColor(getResources().getColor(R.color.color7));//设置文字为浅灰色
            t_one_down.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);//设置字体加粗
            t_one_down.setLayoutParams(t_one_down_layout);//将类别文字装入自己的布局
            layout_three.addView(t_one_down);
            //备注信息编写完毕

            //第四层布局编写，第二个垂直线性布局
            LinearLayout layout_four = new LinearLayout(getContext());//新建
            LinearLayout.LayoutParams four_Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            four_Params.setMargins(0, 5, 0, 0);//设置边距
            layout_four.setOrientation(LinearLayout.VERTICAL);//设为垂直布局
            layout_four.setBackgroundColor(getResources().getColor(R.color.color4));//第四层布局设置为白色背景
            layout_four.setLayoutParams(four_Params);//将上面的大小和边距属性赋给第四层布局
            //第四层布局编写完毕

            //价格文字编写
            TextView t_two_up = new TextView(getContext());//新建
            RelativeLayout.LayoutParams t_two_up_layout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            t_two_up_layout.setMargins(0, 0, 0, 0);//通过设置边距位置达到居中效果
            t_two_up.setGravity(Gravity.RIGHT);
            t_two_up.setText("￥" + double_pay_money);
            t_two_up.setTextSize(20);
            t_two_up.setTextColor(getResources().getColor(R.color.color8));//设置文字为红色
            t_two_up.setLayoutParams(t_two_up_layout);//将价格文字装入自己的布局
            layout_four.addView(t_two_up);//将价格装入第四层布局
            //价格文字编写完毕

            //支出源头文字编写
            TextView t_two_down = new TextView(getContext());//新建
            LinearLayout.LayoutParams t_two_down_layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            t_two_down_layout.setMargins(0, 0, 0, 0);//通过设置边距位置达到居中效果
            t_two_down.setGravity(Gravity.RIGHT);
            t_two_down.setText(string_asset_bankname);
            t_two_down.setTextSize(14);
            t_two_down.setTextColor(getResources().getColor(R.color.color7));//设置文字为浅灰色
            t_two_down.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);//设置字体加粗
            t_two_down.setLayoutParams(t_two_down_layout);//将支出源头文字装入自己的布局
            layout_four.addView(t_two_down);//将支出源头文字装入第四层布局
            //支出源头文字编写完毕

            //横线的编写
            View line = new View(getContext());//新建
            RelativeLayout.LayoutParams line_layout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 5);
            line_layout.setMargins(140, 0, 0, 0);
            line.setLayoutParams(line_layout);//将分割线加入自己的布局
            line.setBackgroundColor(getResources().getColor(R.color.color1));//给横线设置黄色
            //分割横线编写完毕

            layout_two.addView(layout_three);//将第三层装入第二层
            layout_two.addView(layout_four);//将第四层装入第二层
            layout_all.addView(layout_two);//将第二层布局加入动态总布局中
            layout_all.addView(line);//将分割线加入动态总布局
            layout_one.addView(layout_all);//将动态总布局布局加入第一层布局中

            //动态生成编写完毕
        }
    }

}
