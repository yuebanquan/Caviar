package com.example.terminal_work.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.example.terminal_work.R;


public class Home_house_Activity extends AppCompatActivity {

    //下面创建三个管理四个Fragment碎片必要的组建
    private Fragment[] fragments=new Fragment[4];
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    //声明四个碎片类
    private OneFragment oneFragment;
    private TwoFragment twoFragment;
    private ThreeFragment threeFragment;
    private FourFragment fourFragment;
    //声明四个RadioButton
    private RadioButton one_button;
    private RadioButton two_button;
    private RadioButton three_button;
    private RadioButton four_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculate_house_page);//加载房贷计算的布局

        //找到RadioButton
        one_button=findViewById(R.id.one_btn);
        two_button=findViewById(R.id.two_btn);
        three_button=findViewById(R.id.three_btn);
        four_button=findViewById(R.id.four_btn);

        //四个碎片的实例化
        oneFragment=new OneFragment();
        twoFragment=new TwoFragment();
        threeFragment=new ThreeFragment();
        fourFragment=new FourFragment();

        fragmentManager=getSupportFragmentManager();
        //下面将四个Fragment加入到fragments数组中,方便后续使用
        fragments=new Fragment[]{oneFragment,twoFragment,threeFragment,fourFragment};

        selectTab(0);//首先加载第一个首页页面

        one_button.setOnClickListener(new View.OnClickListener() {//绑定监听
            @Override
            public void onClick(View v) {
                selectTab(0);
            }
        });
        two_button.setOnClickListener(new View.OnClickListener() {//绑定监听
            @Override
            public void onClick(View v) {
                selectTab(1);
            }
        });
        three_button.setOnClickListener(new View.OnClickListener() {//绑定监听
            @Override
            public void onClick(View v) {
                selectTab(2);
            }
        });
        four_button.setOnClickListener(new View.OnClickListener() {//绑定监听
            @Override
            public void onClick(View v) {
                selectTab(3);
            }
        });
    }
    private void selectTab(int i) {
        transaction=fragmentManager.beginTransaction();
        //在首页展示之前先隐藏其他页面
        hideAllFragment(transaction);
        //展示需要展示的fragment
        if(fragments[i].isAdded()){//判断是否将fragment加入，如果加入则直接展示即可
            transaction.show(fragments[i]);
        }
        else{//如果未加入，则先加入，后展示
            transaction.add(R.id.mFragment3,fragments[i]);
            transaction.show(fragments[i]);
        }
        transaction.commit();//最后进行事务的提交
    }

    private void hideAllFragment(FragmentTransaction transaction) {//隐藏四个Fragment
        for(int i=0;i<fragments.length;i++){
            transaction.hide(fragments[i]);
        }
    }
}
