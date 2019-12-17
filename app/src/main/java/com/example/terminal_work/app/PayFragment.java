package com.example.terminal_work.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.terminal_work.R;
import com.example.terminal_work.litepal.AssetAccount;
import com.example.terminal_work.litepal.Classify;
import com.example.terminal_work.litepal.Tally;

import org.litepal.LitePal;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PayFragment extends Fragment {
    /**
     * ImageButton add_way 声明支出方式（哪个银行，哪个软件支出）弹出菜单的图标实现按钮
     * TextView change_pay_text 声明需要变更的支出方式的文字
     * RadioGroup rdgp1, rdgp2, rdgp3 声明实现多行多列的radiogroup
     * RadioButton radbtn1, radbtn2, radbtn3, radbtn4, radbtn5,
     * radbtn6, radbtn7, radbtn8, radbtn9, radbtn10, radbtn11, radbtn12   类别按钮
     * Button ok 确认按钮，用于将数据放入数据库
     * <p>
     * String t_comment 备注信息存放变量
     * double t_money 金额存放变量
     * String t_from 支出源头存放变量
     * EditText more 备注信息输入框
     * EditText money 金额输入框
     * String kind="" 类别提取变量，先给个空值,具体的提取写在下面的RadioButton的监听事件里
     * <p>
     * ImageButton date  点击显示日期选择框
     * TimePickerView pvTime    时间选择器
     * Tally tally  记一笔数据库对应表
     */
    private static final String TAG = "PayFragment";

    private RadioGroup rdgp1, rdgp2, rdgp3;//声明实现多行多列的radiogroup
    private Button ok;//用于确认将数据放入数据库
    //数据库存放变量相关
    /**
     * 记一笔Tally表支出方向需要收集(备注信息、支出金额、支出源头、支出类别、时间)五个
     * 支出源头关联AssetAccount表，类别关联Classify表
     */

    private String pay_category = "餐饮";//支出类别提取变量，先给个空值,具体的提取写在下面的RadioButton的监听事件里
    private Date date_time;//支出日期

    private EditText more;//备注信息输入框
    private EditText money;//支出金额输入框
    private ImageButton add_way;//声明弹出菜单的实现按钮（支出源头图片选择框）
    private TextView change_pay_text;//支出源头的文字区
    private RadioButton radbtn1, radbtn2, radbtn3, radbtn4, radbtn5,
            radbtn6, radbtn7, radbtn8, radbtn9, radbtn10, radbtn11, radbtn12;//支出类别按钮
    private RadioButton rb_date;//日期选择以及显示框

    TimePickerView pvTime;      //时间选择器

    //声明数据库表对象
    private static Tally tally;
    private static Classify classify;
    private static AssetAccount assetAccount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflater使将xml布局文件转换为视图的一个类,container表示在container里面显示这个视图
        View view2 = inflater.inflate(R.layout.add_pay, container, false);

        /**
         * 初始化函数
         */
        change_pay_text = view2.findViewById(R.id.pay_way_text);
        add_way = view2.findViewById(R.id.add_way_select);
        more = view2.findViewById(R.id.more);
        money = view2.findViewById(R.id.money);
        ok = view2.findViewById(R.id.ok);
        //去找到Radiogroup和RadioButton
        rdgp1 = view2.findViewById(R.id.radio_group1);
        rdgp2 = view2.findViewById(R.id.radio_group2);
        rdgp3 = view2.findViewById(R.id.radio_group3);
        radbtn1 = view2.findViewById(R.id.food);
        radbtn2 = view2.findViewById(R.id.travel);
        radbtn3 = view2.findViewById(R.id.shop);
        radbtn4 = view2.findViewById(R.id.traffic);
        radbtn5 = view2.findViewById(R.id.communication);
        radbtn6 = view2.findViewById(R.id.hospital);
        radbtn7 = view2.findViewById(R.id.house);
        radbtn8 = view2.findViewById(R.id.child);
        radbtn9 = view2.findViewById(R.id.study);
        radbtn10 = view2.findViewById(R.id.play);
        radbtn11 = view2.findViewById(R.id.pet);
        radbtn12 = view2.findViewById(R.id.interest);

        rb_date = view2.findViewById(R.id.date_btn);

        InputFilter[] filters = {new CashierInputFilter()};//设置金额输入的过滤器，保证只能输入金额类型
        money.setFilters(filters);//限定余额输入框（money）只能输入金额的格式

        //初始化时间选择器
        initTimePick();

        //监听类事件封装
        all_Listene_PayFragment();

        return view2;//返回具体的布局
    }/**
     onCreateView结束
     */

    /**
     * 初始化时间选择器
     */
    private void initTimePick() {
        //时间选择器
        pvTime = new TimePickerBuilder(getContext(), new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                rb_date.setText(format.format(date));

            }
        }).build();
    }

    /**
     * 监听类函数
     */
    private void all_Listene_PayFragment() {
        //1:支出记一笔页面的确定按钮监听事件：将输入数据放入数据库
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //先从输入框提取:备注信息、支出金额、支出源头、支出类别(在类别的radiobutton点击的时候自动赋值给pay_category)、时间
                //最终变量：string_more，Double_money，string_from，pay_category，date_time
                String string_more = more.getText().toString();//提取备注信息输入框
                String string_money = money.getText().toString();//提取输入的金额String
                if (!TextUtils.isEmpty(string_money)) {//如果提取的金额输入不为空（备注信息可以为空）
                    double Double_money = Double.parseDouble(string_money);//将string的金额转化为double
                    Log.d(TAG, "onClick: string_money = "+ string_money);
                    Log.d(TAG, "onClick: Double_money = "+ Double_money);
                    String string_from = change_pay_text.getText().toString();//提取输入的支出来源
                    String string_time = rb_date.getText().toString();//日期栏获取
                    if (string_time.equals("今天")) {//如果为今天（表示日期采用当前日期）
                        Date date = new java.sql.Date(new Date().getTime());//获取当前系统时间（年—月—日）
                        string_time = date.toString();
                    }
                    if (!TextUtils.isEmpty(string_from) && !TextUtils.isEmpty(string_time)) {
                        //前三个输入的提取不为空,就先对日期的String转换为Date类型
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            date_time = sdf.parse(string_time);//用parse的时候需要结合try-catch捕获异常
                            //提取选择的日期并将string类型转换为Date类型
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //类别变量在下面对应的RadioButton的监听里获取
                        //提取完毕，开始编写存入数据库
                        tally = new Tally();
                        tally.setT_Date(date_time);
                        tally.setT_comment(string_more);
                        tally.setT_money(Double_money);
                        classify = LitePal.where("c_type == ?", pay_category).findFirst(Classify.class);
                        classify.getC_tallyList().add(tally);
                        classify.save();
                        assetAccount = LitePal.where("a_bankname == ?", string_from).findFirst(AssetAccount.class);
                        assetAccount.getA_tallyList().add(tally);

                        assetAccount.setA_balance(calculateBalance(assetAccount.getA_balance(), Double_money));        //相应地更改账户余额
//
//                        assetAccount.setA_balance(assetAccount.getA_balance() - Double_money);
                        assetAccount.save();
                        tally.save();


                        //返回主页面
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                } else Toast.makeText(getContext(), "请输入金额", Toast.LENGTH_SHORT).show();
            }
        });
        //2：支出源头图片点击事件：点击显示出po_menu用于选择支出的银行或第三方软件
        add_way.setOnClickListener(new View.OnClickListener() {//弹出菜单按钮点击事件，用于点击产生弹出菜单
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), add_way);//java创建一个弹出菜单
                popup.getMenuInflater().inflate(R.menu.addway_menu_pop, popup.getMenu());//找到布局
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {//当点击弹出菜单里的子项的点击事件
                        switch (item.getItemId()) {
                            case R.id.wechat:
                                add_way.setBackgroundResource(R.drawable.wechat);//图片变更
                                change_pay_text.setText("微信");//文字变更
                                break;
                            case R.id.alipay:
                                add_way.setBackgroundResource(R.drawable.alipay);
                                change_pay_text.setText("支付宝");
                                break;
                            case R.id.icbc:
                                add_way.setBackgroundResource(R.drawable.icbc);
                                change_pay_text.setText("工商银行");
                                break;
                            case R.id.spdb:
                                add_way.setBackgroundResource(R.drawable.spdb);
                                change_pay_text.setText("浦发银行");
                                break;
                            case R.id.cmb:
                                add_way.setBackgroundResource(R.drawable.cmb);
                                change_pay_text.setText("招商银行");
                                break;
                            case R.id.bcm:
                                add_way.setBackgroundResource(R.drawable.bcm);
                                change_pay_text.setText("交通银行");
                                break;
                            case R.id.ccb:
                                add_way.setBackgroundResource(R.drawable.ccb);
                                change_pay_text.setText("建设银行");
                                break;
                            case R.id.boc:
                                add_way.setBackgroundResource(R.drawable.boc);
                                change_pay_text.setText("中国银行");
                                break;
                            case R.id.abc:
                                add_way.setBackgroundResource(R.drawable.abc);
                                change_pay_text.setText("农业银行");
                                break;
                        }
                        return true;
                    }
                });
                try {//利用反射机制强制显示PopMenu里的icon
                    Field field = popup.getClass().getDeclaredField("mPopup");
                    field.setAccessible(true);
                    MenuPopupHelper mHelper = (MenuPopupHelper) field.get(popup);
                    mHelper.setForceShowIcon(true);
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }

                popup.show();//弹出菜单的而显示
            }
        });
        //3:radiobutton绑定监听：每次点击保证了一个按钮亮的同时,提取对应的文字，用于放入数据库
        radbtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdgp2.clearCheck();
                rdgp3.clearCheck();
                pay_category = "餐饮";
            }
        });
        radbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdgp2.clearCheck();
                rdgp3.clearCheck();
                pay_category = "旅行";
            }
        });
        radbtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdgp2.clearCheck();
                rdgp3.clearCheck();
                pay_category = "购物";
            }
        });
        radbtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdgp2.clearCheck();
                rdgp3.clearCheck();
                pay_category = "交通";
            }
        });
        radbtn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdgp1.clearCheck();
                rdgp3.clearCheck();
                pay_category = "通讯";
            }
        });
        radbtn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdgp1.clearCheck();
                rdgp3.clearCheck();
                pay_category = "医疗";
            }
        });
        radbtn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdgp1.clearCheck();
                rdgp3.clearCheck();
                pay_category = "住房";
            }
        });
        radbtn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdgp1.clearCheck();
                rdgp3.clearCheck();
                pay_category = "育儿";
            }
        });
        radbtn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdgp1.clearCheck();
                rdgp2.clearCheck();
                pay_category = "文教";
            }
        });
        radbtn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdgp1.clearCheck();
                rdgp2.clearCheck();
                pay_category = "娱乐";
            }
        });
        radbtn11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdgp1.clearCheck();
                rdgp2.clearCheck();
                pay_category = "宠物";
            }
        });
        radbtn12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdgp1.clearCheck();
                rdgp2.clearCheck();
                pay_category = "生活";
            }
        });

        //3：日期选择按钮：点击i弹出日期选择框
        rb_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示时间选择器
                pvTime.show();
            }
        });
    }//监听类事件封装结束


    /**
     * 记一笔后
     * 计算银行卡余额
     * 返回最终余额(double)
     * <p>
     * originalBalance  原先的余额（double）
     * money            花费的金额（double）
     * big_originalBalance  原先的余额（BigDecimal）
     * big_money            花费的金额（BigDecimal）
     * big_finalMoney   最终的余额
     */
    private double calculateBalance(Double originalBalance, Double money) {
        Log.d(TAG, "calculateBalance: originalBalance = " + originalBalance);
        Log.d(TAG, "calculateBalance: money = " + money);
        BigDecimal big_originalBalance = new BigDecimal(originalBalance.toString());
        BigDecimal big_money = new BigDecimal(money.toString());
        BigDecimal big_finalMoney = big_originalBalance.subtract(big_money);
        Log.d(TAG, "calculateBalance: big_originalBalance" + big_originalBalance);
        Log.d(TAG, "calculateBalance: big_finalMoney = "+ big_finalMoney);
        Log.d(TAG, "calculateBalance: return = "+ big_finalMoney.doubleValue());
        return big_finalMoney.doubleValue();
    }
}