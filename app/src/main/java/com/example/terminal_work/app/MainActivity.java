package com.example.terminal_work.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.terminal_work.R;
import com.example.terminal_work.litepal.AssetAccount;
import com.example.terminal_work.litepal.Classify;
import com.example.terminal_work.litepal.User;
import com.example.terminal_work.litepal.Wish;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //下面创建三个管理四个Fragment碎片必要的组建
    private Fragment[] fragments = new Fragment[4];
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    //声明四个碎片类
    private HomeFragment homeFragment;
    private BillFragment billFragment;
    private WishFragment wishFragment;
    private TableFragment tableFragment;
    //声明底部导航栏的按钮
    private RadioButton homebutton;
    private RadioButton billbutton;
    private RadioButton wishbutton;
    private RadioButton tablebutton;

    private RadioButton add;//对加一笔按钮的声明

    //支出类别名
    static String[] typeOut = {"餐饮", "旅行", "购物", "交通", "通讯",
            "医疗", "住房", "育儿", "文教", "娱乐", "宠物", "生活"};
    //收入类别名
    static String[] typeIn;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化数据库
        initSql();

        //测试
        /*User user = new User();
        user.setU_id(1);
        user.setU_name("admin");
        user.setU_password("123456");
        user.save();
        user.setU_id(2);
        user.setU_name("ad");
        user.setU_password("1234536");
        user.save();*/

        //测试
//        AssetAccount assetAccount=new AssetAccount();
//        assetAccount.setA_cardNumber(123);
//        assetAccount.setA_cardType("借记卡");
//        assetAccount.setA_balance(12);
//        assetAccount.setA_bankName("中国银行");
//        assetAccount.save();


        //下面找到四个底部导航栏的选择按钮
        homebutton = findViewById(R.id.home_page);
        billbutton = findViewById(R.id.bill_page);
        wishbutton = findViewById(R.id.wish_page);
        tablebutton = findViewById(R.id.table_page);

        add = findViewById(R.id.add_button);//找到加一笔按钮

        //找到之后进行监听的绑定
        homebutton.setOnClickListener(this);
        billbutton.setOnClickListener(this);
        wishbutton.setOnClickListener(this);
        tablebutton.setOnClickListener(this);

        add.setOnClickListener(this);//对加一笔按钮绑定监听

        //下面对三个java的Fragment类进行实例化
        homeFragment = new HomeFragment();
        billFragment = new BillFragment();
        wishFragment = new WishFragment();
        tableFragment = new TableFragment();

        fragmentManager = getSupportFragmentManager();
        //下面将四个Fragment加入到fragments数组中,方便后续使用
        fragments = new Fragment[]{homeFragment, billFragment, wishFragment, tableFragment};

        selectTab(0);//首先加载第一个首页页面

    }


    private void selectTab(int i) {
        transaction = fragmentManager.beginTransaction();
        //在首页展示之前先隐藏其他页面
        hideAllFragment(transaction);
        //展示需要展示的fragment
        if (fragments[i].isAdded()) {//判断是否将fragment加入，如果加入则直接展示即可
            transaction.show(fragments[i]);
        } else {//如果未加入，则先加入，后展示
            transaction.add(R.id.mFragment, fragments[i]);
            transaction.show(fragments[i]);
        }
        transaction.commit();//最后进行事务的提交
    }

    private void hideAllFragment(FragmentTransaction transaction) {//隐藏四个Fragment
        for (int i = 0; i < fragments.length; i++) {
            transaction.hide(fragments[i]);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_page:
                selectTab(0);
                break;
            case R.id.bill_page:
                selectTab(1);
                break;
            case R.id.wish_page:
                selectTab(2);
                break;
            case R.id.table_page:
                selectTab(3);
                break;
            case R.id.add_button://对加一笔按钮进行点击事件的编写,目的是给出一个弹窗

                //编写出现弹窗
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);//先通过AlertDialog.Builder创建一个AlertDialog实例
                View view = View.inflate(MainActivity.this, R.layout.add_one, null);
                dialog.setView(view);
                dialog.show();

                //找到弹窗里的三个按钮并设置监听事件,跳转到添加心愿页面
                final Button btn_wish = view.findViewById(R.id.button_wish);
                btn_wish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, WishActivity.class);
                        startActivity(intent);
                    }
                });

                //找到弹窗里的三个按钮并设置监听事件,跳转到记一笔页面
                final Button btn_money = view.findViewById(R.id.button_money);
                btn_money.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, Add_Money_Activity.class);
                        startActivity(intent);

                        finish();
                    }
                });

                //找到弹窗里的三个按钮并设置监听事件,跳转到记资产页面
                final Button btn_asset = view.findViewById(R.id.button_asset);
                btn_asset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, Add_Asset_Activity.class);
                        startActivity(intent);

                        finish();
                    }
                });
                break;
        }
    }

    void initSql() {
        //创建数据库
        SQLiteDatabase db = LitePal.getDatabase();

        /**
         * 初始化类别表
         */

        if (classifyIsNull()) {
            for (int i = 0; i <= typeOut.length - 1; i++) {
                Classify classify = new Classify();
                classify.setC_type(typeOut[i]);
                classify.setC_isIn(false);
                classify.save();
            }
        }
    }

    /**
     * 判断Classify表中有无数据
     */
    private Boolean classifyIsNull(){
        List <Classify> classify = LitePal.findAll(Classify.class);//查询所有值，返回的使一个list集合
        if(classify.size()==0){//判断，如果返回集合的大小为0就说明还没有数据
            return true;
        }
        else {//否则说明有数据
            return false;
        }
    }
}
