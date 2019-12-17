package com.example.terminal_work.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.terminal_work.R;
import com.example.terminal_work.litepal.Wish;
import com.github.mikephil.charting.utils.Utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WishActivity extends AppCompatActivity {
    //日志信息
    private static final String TAG = "WishActivity";

    private TimePickerView pvTime;
    private TextView b_WishMonth;
    private Button next_step;
    private EditText write_wish, wish_money;
    private Button otherWish1, otherWish2, otherWish3;  //其他人的心愿
    private TextView moneyEveryMouth;   //每月需要存多少钱
    String wish_string, money_string, month_string;
    int month_int;

    //心愿名称，地点
    String w_name,w_location;
    int w_month;//心愿实现月份
    double w_money;//心愿金额

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化WishActivity
        initWishActivity();

        //初始化月份选择器
        initTimePickerView();

        //点击“看看大家心愿”下的按钮自动填充数据
        getOtherWish();

        //计算每月需要存多少钱
        calculateMoney();

        //获取输入的值并存入数据库


        //点击“下一步”进入显示心愿页面
        //判断机制
        //数据库插入
        next();

    }//onCreate结束


    /*********************
     * 初始化WishActivity函数
     **********************/
    private void initWishActivity() {
        setContentView(R.layout.add_wish_page);//加载添加心愿的布局

        //点击"所需月份数"按钮弹出月份选择器
        b_WishMonth = findViewById(R.id.b_WishMonth);
        write_wish = findViewById(R.id.write_wish);
        wish_money = findViewById(R.id.wish_money);
        next_step = findViewById(R.id.next_step);//找到下一步这个按钮

        //设置金额输入的过滤器，保证只能输入金额类型
        InputFilter[] filters = {new CashierInputFilter()};
        wish_money.setFilters(filters);

        //点击月份选择器选择月份后显示月数，并计算每月需要金额
        b_WishMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show(v);

                /**
                 * 待写
                 * 并计算每月需要金额
                 */


            }
        });

    }

    /**********************************
     *心愿需要完成月份选择器，初始化函数
     * **********************************/
    private void initTimePickerView() {
        //时间选择器
        pvTime = new TimePickerBuilder(WishActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                //选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                /*btn_Time.setText(getTime(date));*/
                b_WishMonth.setText(getTime(date) + "个月");
            }
        })
                .setType(new boolean[]{false, true, false, false, false, false})    //只显示月份
//                .setTitleSize()               //标题文字大小
                .setTitleText("选择所需的月份数")//标题文字
                .setContentTextSize(20)        //滚轮文字大小
                .setOutSideCancelable(true)    //点击控件外部范围时，取消显示
                .isCyclic(true)                //是否循环滚动
                .isDialog(true)                //是否显示为对话框样式
                .build();

    }

    private String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("M");
        return format.format(date);
    }

    /*******************************************
     * 点击“看看大家心愿”下的按钮自动填充数据
     ******************************************/
    private void getOtherWish() {
        //三个其他人的心愿
        otherWish1 = (Button) findViewById(R.id.otherWish1);
        otherWish2 = (Button) findViewById(R.id.otherWish2);
        otherWish3 = (Button) findViewById(R.id.otherWish3);

        //点击“来一场旅行”
        otherWish1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write_wish.setText("来一场旅行");
                wish_money.setText("10000.00");
            }
        });

        //点击“换一部新手机”
        otherWish2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write_wish.setText("换一部新手机");
                wish_money.setText("4999.00");
            }
        });

        //点击“送TA一份礼物”
        otherWish3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write_wish.setText("送TA一份礼物");
                wish_money.setText("884.80");
            }
        });
    }

    /************************************
     * 点击“下一步”进入显示心愿页面
     * 判断机制
     * 数据库插入
     ************************************/
    private void next() {
        //点击下一步之后进入显示心愿页面
        next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击下一步之后进入显示心愿页面
                insert_Wish();//将输入的数据放入数据库
            }
        });
    }

    /**
     * 如果金额和月份都输入的话，计算每月需要存多少钱，并显示出来
     */
    private void calculateMoney() {
        moneyEveryMouth = (TextView) findViewById(R.id.moneyEveryMouth);

        //测试正则表达式是否正常
        BigDecimal testMonth = new BigDecimal(month_int);   //月份
        Log.d(TAG, "calculateMoney: " + testMonth);


        //输入金额时的监听事件
        wish_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    month_int = extractNum(b_WishMonth.getText().toString());
//                    Log.d(TAG, "onTextChanged: "+month_int);
                    money_string = wish_money.getText().toString();

                    if ((month_int <= 12 && month_int >= 1)) {
                        //日志，查看输入的金额和月份
                        Log.d(TAG, "输入中的月份: " + month_int);
                        Log.d(TAG, "输入中的金额: " + money_string);

                        BigDecimal totalMoney = new BigDecimal(money_string);  //总金额
                        BigDecimal month = new BigDecimal(month_int);   //月份
                        BigDecimal money;   //每月需要金额
                        money = totalMoney.divide(month);
                        moneyEveryMouth.setText(money + "");
                        Log.d(TAG, "计算后的金额: " + money);

                    }
                } catch (Exception e) {

                }
            }
        });


    }

    /**
     * 正则表达式提取出String中的数字
     * 用于提取month_string中的月份数
     * 并转换为int类型
     */
    public int extractNum(String str) {
        int number;
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(str);
        str = matcher.replaceAll("");
        number = Integer.parseInt(str);
        return number;
    }

    /**
     * 将输入的心愿放入数据库,也是下一步按钮的点击事件
     */
    private void insert_Wish() {
        // 获取当时的时间并将格林威治时间进行转化
        Date date = new  java.sql.Date(new Date().getTime());
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        w_name = write_wish.getText().toString();//获取心愿名称
        String string_w_money = wish_money.getText().toString();//获取输入需要实现的金额
        String string_w_month=b_WishMonth.getText().toString();
        if(!string_w_month.equals("选择所需的月份数▼")) {
            w_month = extractNum(string_w_month);//获取实现的月份数目

            if (w_month > 0 && w_month < 13 && !TextUtils.isEmpty(w_name) && !TextUtils.isEmpty(string_w_money)) {
                //如果三个输入的内容都不为空的话，开始类型转换并存入数据库
                w_money = Double.parseDouble(string_w_money);
                Wish wish = new Wish();
                wish.setW_name(w_name);
                Log.d(TAG, "成功存入心愿名字" + w_name);
                wish.setW_money(w_money);
                Log.d(TAG, "成功存入心愿预算" + w_money);
                wish.setW_date(date);//放入最终的日期形式
                Log.d(TAG, "成功存入心愿日期" + date);
                wish.setW_month(w_month);
                Log.d(TAG, "成功存入心愿实现月份" + w_month);
                wish.save();
                Intent intent=new Intent(this,Show_Wish_Activity.class);
                startActivity(intent);
            }
            else Toast.makeText(WishActivity.this, "请先输入完整的心愿信息", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(WishActivity.this, "请先输入完整的心愿信息", Toast.LENGTH_SHORT).show();


    }

}

