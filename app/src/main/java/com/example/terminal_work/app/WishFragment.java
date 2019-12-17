package com.example.terminal_work.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.terminal_work.R;


public class WishFragment extends Fragment {

    private Button add_wish;
    private Button show_wish;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //inflater使将xml布局文件转换为视图的一个类,container表示在container里面显示这个视图
        View view = inflater.inflate(R.layout.wish_page, container, false);

        add_wish=view.findViewById(R.id.save_money_button);//找到添加心愿这个按钮
        show_wish=view.findViewById(R.id.show_dream_button);//找到显示心愿这个按钮

        add_wish.setOnClickListener(new View.OnClickListener() {//编写心愿界面的按钮点击事件
            @Override
            public void onClick(View v) {//点击事件为启动一个新的Activity
                Intent intent = new Intent(getContext(), WishActivity.class);
                startActivity(intent);
            }
        });

        show_wish.setOnClickListener(new View.OnClickListener() {//编写心愿界面的按钮点击事件
            @Override
            public void onClick(View v) {//点击事件为启动一个新的Activity
                Intent intent = new Intent(getContext(), Show_Wish_Activity.class);
                startActivity(intent);
            }
        });

            return view;//返回具体的布局
        }
}
