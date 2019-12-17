package com.example.terminal_work.app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.terminal_work.R;
import com.example.terminal_work.litepal.AssetAccount;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 添加资产Activity
 */
public class Add_Asset_Activity extends AppCompatActivity {
    /**
     * Intent intent 跳转到MainActivity的intent
     * Spinner spinner 找到布局里的spinner，用于选择添加的币种
     * SimpleAdapter adapter给spinner加图片和文字的适配器
     * List<Map<String, Object>> dataList spinner的数据源
     * int[] icon 封装币种的国旗图片数据源,用于spinner中
     * String[] iconName封装币种的英文简写数据源，用于spinner中
     * LinearLayout majoy_layout声明动态添加币种的最外层布局
     * TextView select_card 信用卡/借记卡/其他 的选择卡类别的TextView
     *ImageButton select_bank选择资产来源的银行或第三方软件（微信，支付宝）
     * Button back 当回按钮
     * Button[] buttons = new Button[16] 定义16个动态生成删除功能按钮数组
     * int i = 0 用于确定删除功能,类似滚轮的功能
     * int aud = 0, mop = 0 记录生成位置的标记，暂时只做了删除澳门币和澳币的标记，后期更改方法，不能用标记来做（涉及到算法问题）
     * Button save 保存按钮，将输入的数据放入数据库
     * EditText more_message 备注信息输入框
     * EditText card_number 卡号信息输入框
     * EditText bank_name 银行或第三方资产来源输入框
     * EditText balanace卡余额输入框
     * AssetAccount assetAccount 数据库的资产表对象
     */
    private Intent intent;
    private Spinner spinner;
    private SimpleAdapter adapter; //适配器
    private List<Map<String, Object>> dataList;
    private int[] icon = {R.drawable.cny_circle, R.drawable.aud_circle, R.drawable.mop_circle, R.drawable.brl_circle
            , R.drawable.cad_circle, R.drawable.dkk_circle, R.drawable.eur_circle, R.drawable.gbp_circle
            , R.drawable.hkd_circle, R.drawable.inr_circle, R.drawable.khr_circle, R.drawable.nzd_circle,
            R.drawable.thb_circle, R.drawable.usd_circle, R.drawable.vnd_circle, R.drawable.sek_circle};//图片源
    private String[] iconName = {"人民币", "澳币", "澳门币", "巴西雷亚尔", "加拿大元", "丹麦克朗", "欧元"
            , "英镑", "港币", "印度卢比", "柬埔寨瑞尔", "新西兰元", "泰铢", "美元", "越南盾", "瑞典克朗"};//文字源
    private LinearLayout majoy_layout;//声明动态添加控件的装入布局
    private TextView select_card;//信用卡/借记卡选择按钮
    private ImageButton select_bank;
    private Button back;//返回按钮
    Button[] buttons = new Button[16];//定义删除功能按钮数组
    static int i = 0;//用于确定删除功能,类似滚轮的功能
    int aud = 0, mop = 0;//记录生成位置的标记
    private Button save;
    private EditText more_message,card_number,bank_name,balanace;
    private static AssetAccount assetAccount;

    /**
     * 重写返回键，使得点击返回键可以重新加载MainActivity
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //跳转到MainActivity的intent
        final Intent intent = new Intent(Add_Asset_Activity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add__asset);

        //初始化所有声明的变量控件
        initAdd_Asset_Activity();

        //数据源的装入，与绑定适配器
        dataList = new ArrayList<Map<String, Object>>();//创建数据源
        adapter = new SimpleAdapter(this, getData(), R.layout.add_asset_spinner,
                new String[]{"spinner_image", "spinner_text"}, new int[]{R.id.spinner_image, R.id.spinner_text});//创建简单适配器
        adapter.setDropDownViewResource(R.layout.add_asset_spinner);//adapter设置一个下拉列表样式，参数为自己定义的子布局
        spinner.setAdapter(adapter); //spinner加载适配器

        //监听事件的功能封装,所有的监听都放在里面
        all_Listene_Add_Asset_Activity();

    }/**
     onCreate结束
 */

    /**
     * 初始化函数
     */
    private void initAdd_Asset_Activity(){
        back = findViewById(R.id.back);//找到返回键
        save = findViewById(R.id.save);//找到保存，将信息录入数据库的按钮
        select_bank = findViewById(R.id.select_bank);//找到选择资产来源的按钮
        majoy_layout = findViewById(R.id.majoy_layout);//找到动态添加的xml总布局
        spinner = findViewById(R.id.add_asset_spinner);//找到选择币种的spinner
        more_message = findViewById(R.id.more_message);//找到备注信息输入框
        card_number = findViewById(R.id.card_number);//找到卡号输入框
        bank_name = findViewById(R.id.bank_name);//找到资产来源输入框
        balanace = findViewById(R.id.balanace);//找到余额输入框
        select_card = findViewById(R.id.select_card);//找到选择卡类别的TextView
        more_message.setText("借记卡");//给备注信息和资产来源一个初始值,与xml里的hint是不同的！！
        bank_name.setText("中国银行");//给备注信息和资产来源一个初始值,与xml里的hint是不同的！！
        intent = new Intent(Add_Asset_Activity.this, MainActivity.class);//跳转到MainActivity的intent
        InputFilter[] filters={new CashierInputFilter()};//设置金额输入的过滤器，保证只能输入金额类型
        balanace.setFilters(filters);//限定余额输入框（balanace）只能输入金额的格式
    }//初始化函数结束


    /**
     * 监听类函数
     */
    //监听事件的封装函数
    private void all_Listene_Add_Asset_Activity(){
        //1:保存按钮的监听：点击后将信息提取并保存到数据库，同时返回首页fragment
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bankName = bank_name.getText().toString();//提取银行名称
                String card_number_string = card_number.getText().toString();
                if (!TextUtils.isEmpty(card_number_string)) {
                    //提取转换出来的String的判断方式！不能用(if something!=null)或者(ifsomething="")来判断
                    long card_Number = Long.parseLong(card_number_string);//提取卡号名称
                    String balance_string = balanace.getText().toString();
                    if (!TextUtils.isEmpty(balance_string)) {//如果余额输入不为空
                        double Balanace = Double.parseDouble(balance_string);//提取余额名称
                        String select_Card = select_card.getText().toString();//提取卡类别
                        assetAccount = new AssetAccount();
                        assetAccount.setA_cardNumber(card_Number);
                        assetAccount.setA_cardType(select_Card);
                        assetAccount.setA_balance(Balanace);
                        assetAccount.setA_bankName(bankName);
                        assetAccount.save();
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(Add_Asset_Activity.this,
                                "余额不能为空！请输入余额", Toast.LENGTH_SHORT).show();
                    }
                }
                //因为卡号为主键，如果为空，则阻断往数据库里面存，并用一个广播提示
                else {
                    Toast.makeText(Add_Asset_Activity.this,
                            "卡号不能为空！请输入卡号"  , Toast.LENGTH_SHORT).show();
                }
            }
        });
        //2:选择资产来源的按钮监听，点击后弹出pop_menu设置弹出菜单的点击事件
        select_bank.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Add_Asset_Activity.this, select_bank);//java创建一个弹出菜单
                popup.getMenuInflater().inflate(R.menu.select_bank_pop, popup.getMenu());//找到布局
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {//当点击弹出菜单里的子项的点击事件
                        switch (item.getItemId()) {
                            case R.id.icbc:
                                select_bank.setBackgroundResource(R.drawable.icbc);//图片变更
                                bank_name.setText("工商银行");// 同时底下的银行名称也变
                                break;
                            case R.id.spdb:
                                select_bank.setBackgroundResource(R.drawable.spdb);
                                bank_name.setText("浦发银行");
                                break;
                            case R.id.cmb:
                                select_bank.setBackgroundResource(R.drawable.cmb);
                                bank_name.setText("招商银行");
                                break;
                            case R.id.bcm:
                                select_bank.setBackgroundResource(R.drawable.bcm);
                                bank_name.setText("交通银行");
                                break;
                            case R.id.ccb:
                                select_bank.setBackgroundResource(R.drawable.ccb);
                                bank_name.setText("建设银行");
                                break;
                            case R.id.boc:
                                select_bank.setBackgroundResource(R.drawable.boc);
                                bank_name.setText("中国银行");
                                break;
                            case R.id.abc:
                                select_bank.setBackgroundResource(R.drawable.abc);
                                bank_name.setText("农业银行");
                                break;
                            case R.id.wechat:
                                select_bank.setBackgroundResource(R.drawable.wechat);
                                bank_name.setText("微信");
                                break;
                            case R.id.alipay:
                                select_bank.setBackgroundResource(R.drawable.alipay);
                                bank_name.setText("支付宝");
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
        //3:返回按钮的监听：点击后返回之前的活动，并结束当前活动
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
                finish();//结束当前activity
            }
        });
        //4：选择卡类别的TextView监听：点击之后弹出pop_menu，用于选择卡的类别，与备注信息输入框挂钩
        select_card.setOnClickListener(new View.OnClickListener() {//点击出现对应的popmenu
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(Add_Asset_Activity.this, select_card);//java创建一个弹出菜单
                popup.getMenuInflater().inflate(R.menu.select_card_pop, popup.getMenu());//找到布局
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {//当点击弹出菜单里的子项的点击事件
                        switch (item.getItemId()) {
                            case R.id.credit_card:
                                select_card.setText("信用卡");//文字变更
                                more_message.setText("信用卡");//同时下面的备注信息也跟着变
                                break;
                            case R.id.debit_card:
                                select_card.setText("借记卡");//文字变更
                                more_message.setText("借记卡");//同时下面的备注信息也跟这边
                                break;
                            case R.id.other:
                                select_card.setText("其他");//文字变更
                                more_message.setText("");//同时下面的备注信息也跟这边
                                break;
                        }
                        return true;
                    }
                });
                popup.show();//弹出菜单的而显示
            }
        });
        //5：添加币种spinner点击事件监听以及实现点击spinner选项的触发事件函数
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //动态添加货币种类控件相关
                LinearLayout layout = new LinearLayout(Add_Asset_Activity.this);//构造动态添加的最外围的布局
                EditText[] edittexts = new EditText[16];//定义输入框数组
                ImageView country = new ImageView(Add_Asset_Activity.this);//定义ImageView
                RelativeLayout.LayoutParams btnAddParam = new RelativeLayout.LayoutParams(100, 100);//按钮的自身布局
                RelativeLayout.LayoutParams editAddParam = new RelativeLayout.LayoutParams(
                        820, ViewGroup.LayoutParams.MATCH_PARENT);//输入框的自身布局
                RelativeLayout.LayoutParams imageAddParam = new RelativeLayout.LayoutParams(
                        165, 110);//国旗图标的自身布局

                switch (position) {//编写外围布局中的控件
                    case 1:
                        //点击了第一项后，i值变化
                        i += 1;
                        aud = i;
                        buttons[0] = new Button(Add_Asset_Activity.this);//新建最左边的Button
                        buttons[0].setLayoutParams(btnAddParam);//将button加到它自己的布局中
                        buttons[0].setBackgroundResource(R.drawable.ic_remove_circle_black_24dp);//给button引用资源图片

                        buttons[0].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                removeView(aud);
                            }
                        });

                        layout.addView(buttons[0]);//加到外围布局中
                        edittexts[0] = new EditText(Add_Asset_Activity.this);//中间的写入余额框
                        edittexts[0].setHint("￥0.00");
                        edittexts[0].setLayoutParams(editAddParam);//将输入框加到自己的布局中
                        layout.addView(edittexts[0]);//加到外围布局中
                        country.setBackgroundResource(R.drawable.aud_circle);
                        country.setLayoutParams(imageAddParam);//将图片显示加到自己的布局中
                        layout.addView(country);//加到外围布局中
                        majoy_layout.addView(layout);//将外围布局加到总布局中
                        break;
                    case 2:
                        i += 1;
                        mop = i;
                        buttons[1] = new Button(Add_Asset_Activity.this);//新建最左边的Button
                        buttons[1].setLayoutParams(btnAddParam);//将button加到它自己的布局中
                        buttons[1].setBackgroundResource(R.drawable.ic_remove_circle_black_24dp);//给button引用资源图片
                        buttons[1].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                removeView(mop);
                            }
                        });
                        layout.addView(buttons[1]);//加到外围布局中
                        edittexts[1] = new EditText(Add_Asset_Activity.this);//中间的写入余额框
                        edittexts[1].setHint("￥0.00");
                        edittexts[1].setLayoutParams(editAddParam);//将输入框加到自己的布局中
                        layout.addView(edittexts[1]);//加到外围布局中
                        country.setBackgroundResource(R.drawable.mop_circle);
                        country.setLayoutParams(imageAddParam);//将图片显示加到自己的布局中
                        layout.addView(country);//加到外围布局中
                        majoy_layout.addView(layout);//将外围布局加到总布局中
                        break;
                    case 3:
                        i += 1;
                        buttons[2] = new Button(Add_Asset_Activity.this);//新建最左边的Button
                        buttons[2].setLayoutParams(btnAddParam);//将button加到它自己的布局中
                        buttons[2].setBackgroundResource(R.drawable.ic_remove_circle_black_24dp);//给button引用资源图片
                        layout.addView(buttons[2]);//加到外围布局中
                        edittexts[2] = new EditText(Add_Asset_Activity.this);//中间的写入余额框
                        edittexts[2].setHint("￥0.00");
                        edittexts[2].setLayoutParams(editAddParam);//将输入框加到自己的布局中
                        layout.addView(edittexts[2]);//加到外围布局中
                        country.setBackgroundResource(R.drawable.brl_circle);
                        country.setLayoutParams(imageAddParam);//将图片显示加到自己的布局中
                        layout.addView(country);//加到外围布局中
                        majoy_layout.addView(layout);//将外围布局加到总布局中
                        break;
                    case 4:
                        i += 1;
                        buttons[3] = new Button(Add_Asset_Activity.this);//新建最左边的Button
                        buttons[3].setLayoutParams(btnAddParam);//将button加到它自己的布局中
                        buttons[3].setBackgroundResource(R.drawable.ic_remove_circle_black_24dp);//给button引用资源图片
                        layout.addView(buttons[3]);//加到外围布局中
                        edittexts[3] = new EditText(Add_Asset_Activity.this);//中间的写入余额框
                        edittexts[3].setHint("￥0.00");
                        edittexts[3].setLayoutParams(editAddParam);//将输入框加到自己的布局中
                        layout.addView(edittexts[3]);//加到外围布局中
                        country.setBackgroundResource(R.drawable.cad_circle);
                        country.setLayoutParams(imageAddParam);//将图片显示加到自己的布局中
                        layout.addView(country);//加到外围布局中
                        majoy_layout.addView(layout);//将外围布局加到总布局中
                        break;
                    case 5:
                        i += 1;
                        buttons[4] = new Button(Add_Asset_Activity.this);//新建最左边的Button
                        buttons[4].setLayoutParams(btnAddParam);//将button加到它自己的布局中
                        buttons[4].setBackgroundResource(R.drawable.ic_remove_circle_black_24dp);//给button引用资源图片
                        layout.addView(buttons[4]);//加到外围布局中
                        edittexts[4] = new EditText(Add_Asset_Activity.this);//中间的写入余额框
                        edittexts[4].setHint("￥0.00");
                        edittexts[4].setLayoutParams(editAddParam);//将输入框加到自己的布局中
                        layout.addView(edittexts[4]);//加到外围布局中
                        country.setBackgroundResource(R.drawable.dkk_circle);
                        country.setLayoutParams(imageAddParam);//将图片显示加到自己的布局中
                        layout.addView(country);//加到外围布局中
                        majoy_layout.addView(layout);//将外围布局加到总布局中
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {//什么也没选的时候
                Toast.makeText(Add_Asset_Activity.this, "还没点击",
                        Toast.LENGTH_SHORT).show();//用广播测试先
            }
        });
    }//监听事件封装函数结束

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //设置数据源函数
    private List<Map<String, Object>> getData() {
        for (int i = 0; i < icon.length; i++) {//循环添加图片文字信息  
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("spinner_image", icon[i]);//加入图片
            map.put("spinner_text", iconName[i]);//加入文字
            dataList.add(map);//数据源加入
        }
        return dataList;
    }

    //动态删除组件（按钮）方法
    private void removeView(int i) {
        //获取linearlayout子view的个数
        int count = majoy_layout.getChildCount();
        //研究整个LAYOUT布局，第0位的是含add和remove两个button的layout
        //第count-1个是那个文字被置中的textview
        //因此，在remove的时候，只能操作的是0<location<count-1这个范围的
        //在执行每次remove时，我们从count-1的位置,也就是最后一个
        if (count - 1 > 0) {
            majoy_layout.removeViewAt(i);//删除的核心函数
            if (i == aud) {//删除的是aud
                Add_Asset_Activity.i -= 1;//总个数要-1
                if (aud < mop)//如果aud的位置在mop的前面
                {
                    mop -= 1;
                }//往前挪动一个位置
                else {
                }//否则aud就在mop的后面
            }
            if (i == mop) {
                Add_Asset_Activity.i -= 1;//总个数减一
                if (aud < mop) {
                }//如果aud的位置在mop的前面,nothing
                else {
                    aud -= 1;//否则aud在mop后面，aud-1
                }
            }
        }
    }
}
