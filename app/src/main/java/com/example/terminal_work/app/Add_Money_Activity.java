package com.example.terminal_work.app;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import com.example.terminal_work.R;
import java.util.ArrayList;
import java.util.List;

public class Add_Money_Activity extends AppCompatActivity{
    /**
     * ViewPager vpger 声明记一笔页面中的支出与收入的滑动viewpager
     * List<Fragment> pages=new ArrayList<>() 放置ViewPager中的两个fragment的list
     * RadioButton pay_btn  支出选择按钮，切换到支出页面
     * RadioButton income_btn 收入选择按钮，切换到收入页面
     *
     */
    private ViewPager vpger;//编写ViewPager相关滑动切换实现所需空间
    private List<Fragment> pages=new ArrayList<>();
    private RadioButton pay_btn,income_btn;

    /**
     * 重写返回键，使得点击返回键可以重新加载MainActivity
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //跳转到MainActivity的intent
        final Intent intent = new Intent(Add_Money_Activity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add__money);

        //初始化所有声明的变量控件
        initAdd_Moeny_Activity();

        //监听事件封装函数
        all_Listene_Add_Money_Activity();
    }/**
     onCreate结束
 */

    /**
     * 初始化函数
     */
    private void initAdd_Moeny_Activity(){
        pay_btn=findViewById(R.id.pay_button);//找到支出切换的按钮
        income_btn=findViewById(R.id.income_button);//找到收入切换的按钮
        vpger=findViewById(R.id.view_pager);//找到ViewPager
        //先将两个碎片类实例化并加入
        pages.add(new PayFragment());
        pages.add(new IncomeFragment());
        vpger.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {//给ViewPager添加适配器
            @Override
            public Fragment getItem(int i) {
                return pages.get(i);//确定返回哪个页面
            }

            @Override
            public int getCount() {
                return pages.size();
            }
        });
    }

    /**
     * 监听类函数
     */
    //所有监听事件的封装
    //1：选择支出按钮的监听事件，点击后切换到记支出页面
    private void all_Listene_Add_Money_Activity() {
        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpger.setCurrentItem(0, true);
            }
        });
        //2：选择收入按钮的监听事件，点击后切换到记收入页面
        income_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vpger.setCurrentItem(1,true);
            }
        });
        //3：ViewPager的监听事件，当滑动完成后，上面的按钮状态变化
        vpger.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {//给ViewPager设置监听
            @Override
            public void onPageScrolled(int i, float v, int i1) {//当页面滚动时候触发
            }
            @Override
            public void onPageSelected(int i) {//当滑动完成之后触发
                switch(i){
                    case 0://当为第一个页面的时候
                        pay_btn.setChecked(true);
                        break;
                    case 1://当为第二个页面的时候
                        income_btn.setChecked(true);
                        break;
                }
            }
            @Override
            public void onPageScrollStateChanged(int i) {//当滑动页面状态改变的时候
            }
        });
    }
}
