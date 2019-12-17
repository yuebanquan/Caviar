package com.example.terminal_work.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.terminal_work.R;
import com.example.terminal_work.litepal.AssetAccount;
import com.example.terminal_work.litepal.User;

import org.litepal.LitePal;

import java.util.List;

public class Home_login_Activity extends AppCompatActivity {

    private Button to_regiser;
    private Button login;
    private EditText id,password;
    static public String search_name;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_login_page);//加载注册的布局

        to_regiser=findViewById(R.id.ret_register);//找到从登录页面切换到注册页面的按钮
        login=findViewById(R.id.login);
        id=findViewById(R.id.login_id);
        password=findViewById(R.id.login_password);


        to_regiser.setOnClickListener(new View.OnClickListener() {//并为它绑定点击监听
            @Override
            public void onClick(View v) {
                //打开到注册的Activity活动页面
                Intent intent = new Intent(Home_login_Activity.this, Home_register_Activity.class);
                startActivity(intent);
                finish();//结束自己的activity活动页面
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string_id=id.getText().toString();
                String string_password=password.getText().toString();
                if(!TextUtils.isEmpty(string_id)&&!TextUtils.isEmpty(string_password)){
                    //如果提取的两个输入框内容都不为空,再进行下一步判断
                    List<User> list=LitePal.where("U_id=?",string_id).find(User.class);
                    //上面这句话相当于select U_password from User where U_id=string_id;
                    if(string_password.equals(list.get(0).getU_password())) {
                        String string_name=list.get(0).getU_name();
                        search_name=string_name;
                        Intent intent = new Intent();
                        intent.putExtra("successful_to_log","successful_to_log" );
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                    else Toast.makeText(Home_login_Activity.this,"用户名或密码错误",Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(Home_login_Activity.this,
                        "请输入用户名和密码进行登录", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
