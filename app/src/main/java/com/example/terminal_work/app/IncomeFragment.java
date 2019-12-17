package com.example.terminal_work.app;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.example.terminal_work.R;
import java.lang.reflect.Field;

public class IncomeFragment extends Fragment{

    private RadioGroup rdgp1,rdgp2;
    private RadioButton rd_btn1,rd_btn2,rd_btn3,rd_btn4,rd_btn5,rd_btn6,rd_btn7,rd_btn8;

    private ImageButton add_way2;
    private TextView change_inconme_text;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view2 = inflater.inflate(R.layout.add_income, container, false);

        add_way2=view2.findViewById(R.id.add_way_select2);
        change_inconme_text=view2.findViewById(R.id.pay_way_text);

        add_way2.setOnClickListener(new View.OnClickListener() {//弹出菜单按钮点击事件，用于点击产生弹出菜单
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getContext(), add_way2);//java创建一个弹出菜单
                popup.getMenuInflater().inflate(R.menu.addway_menu_pop, popup.getMenu());//找到布局
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {//当点击弹出菜单里的子项的点击事件
                        switch (item.getItemId()) {
                            case R.id.wechat:
                                add_way2.setBackgroundResource(R.drawable.wechat);//图片变更
                                change_inconme_text.setText("微信");//文字变更
                                break;
                            case R.id.alipay:
                                add_way2.setBackgroundResource(R.drawable.alipay);
                                change_inconme_text.setText("支付宝");
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

        //找到RadioGroup和RadioButton
        rdgp1=view2.findViewById(R.id.radio_group4);
        rdgp2=view2.findViewById(R.id.radio_group5);
        rd_btn1=view2.findViewById(R.id.money);
        rd_btn2=view2.findViewById(R.id.salary);
        rd_btn3=view2.findViewById(R.id.invest_income);
        rd_btn4=view2.findViewById(R.id.repay);
        rd_btn5=view2.findViewById(R.id.borrow);
        rd_btn6=view2.findViewById(R.id.invest_recover);
        rd_btn7=view2.findViewById(R.id.collect_debts);
        rd_btn8=view2.findViewById(R.id.red_paper);
        //编写监听功能
        rd_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdgp2.clearCheck();
            }
        });
        rd_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdgp2.clearCheck();
            }
        });
        rd_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdgp2.clearCheck();
            }
        });
        rd_btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdgp2.clearCheck();
            }
        });
        rd_btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdgp1.clearCheck();
            }
        });
        rd_btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdgp1.clearCheck();
            }
        });
        rd_btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdgp1.clearCheck();
            }
        });
        rd_btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdgp1.clearCheck();
            }
        });
        return view2;
    }
}
