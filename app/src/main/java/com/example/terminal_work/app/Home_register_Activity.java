package com.example.terminal_work.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.terminal_work.R;
import com.example.terminal_work.litepal.User;
import com.github.mikephil.charting.utils.Utils;

public class Home_register_Activity extends AppCompatActivity {

    private Button to_login;
    //输入框的用户名、密码、确认密码、用户账号
    EditText register_name,register_password,makesure_password,register_id;
    private Button make_sure;//注册按钮

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_register_page);//加载注册的布局

        register_name=findViewById(R.id.register_name);
        register_password=findViewById(R.id.register_password);
        makesure_password=findViewById(R.id.makesure_password);
        register_id=findViewById(R.id.register_id);
        make_sure=findViewById(R.id.make_sure);

        to_login=findViewById(R.id.log_in);//找到从注册页面转去登录页面的按钮
        to_login.setOnClickListener(new View.OnClickListener() {//给它设置监听，跳转到登录的activiyty活动页面，同时结束自己的activity
            @Override
            public void onClick(View v) {
                //实现跳转到登录的activity
                Intent intent=new Intent(Home_register_Activity.this,Home_login_Activity.class);
                startActivity(intent);
                finish();//结束自己的activity
            }
        });

        make_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string_name=register_name.getText().toString();
                String string_password=register_password.getText().toString();
                String string_make_sure_password=makesure_password.getText().toString();
                String string_id=register_id.getText().toString();
                if(!TextUtils.isEmpty(string_name)&&!TextUtils.isEmpty(string_password)
                        &&!TextUtils.isEmpty(string_make_sure_password)&&!TextUtils.isEmpty(string_id)){
                    //如果输入都不为空,判断密码和确认密码是否一致
                    // 如果一致就进行类型转换
                    if(string_password.equals(string_make_sure_password)) {
                        User user = new User();
                        int id = Integer.parseInt(string_id);//用户id类型转换为int
                        user.setU_id(id);
                        user.setU_name(string_name);
                        user.setU_password(string_password);
                        user.save();
                        Toast.makeText(Home_register_Activity.this,"注册成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(Home_register_Activity.this,Home_login_Activity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                        Toast.makeText(Home_register_Activity.this, "密码和确认密码不一致", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(Home_register_Activity.this,"请输入完整的注册信息",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
