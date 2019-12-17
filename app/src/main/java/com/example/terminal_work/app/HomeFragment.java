package com.example.terminal_work.app;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.terminal_work.R;
import com.example.terminal_work.litepal.AssetAccount;
import com.example.terminal_work.litepal.User;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static java.lang.String.format;

public class HomeFragment extends Fragment {

    private ListView listview;//声明侧滑菜单的列表控件
    private ArrayAdapter<String> adapter;//声明一个字符串型的数组适配器
    String[] home_menu;//侧滑菜单的内容

    private Button show_list_btn;//声明点击出现侧滑菜单的按钮
    private DrawerLayout drawerLayout;//声明侧滑菜单对应的根布局

    private static final int SELECT_MONEY_UP = 1;
    private static final int SELECT_MONEY_DOWN = 2;
    private static final int TOLOG = 3;

    private EditText up, down;
    String up_string;
    String down_string;
    double up_double = 0;
    double down_double = 0;
    double result = 0;

    ImageButton up_btn;
    ImageButton down_btn;

    public static Dialog mDialog;

    private LinearLayout first_layout;

//    List<AssetAccount> allAsset = LitePal.findAll(AssetAccount.class);


    private String get_bank_name;//获取银行名称
    private String get_more_message;//获取银行名称
    double get_balance;//获取金额
    private TextView own_asset;//净资产
    private TextView all_asset;//总资产

    //请在主线程中声明LocationClient类对象，
    // 该对象初始化需传入Context类型参数。推荐使用getApplicationConext()方法获取全进程有效的Context。
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private BDLocation dblocation;

    private TextView show_location;

    private LinearLayout drawlayout_second;//侧滑菜单栏的布局


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //inflater使将xml布局文件转换为视图的一个类,container表示在container里面显示这个视图
        View view = inflater.inflate(R.layout.home_page, container, false);
        first_layout = view.findViewById(R.id.first_layout);//找到动态添加资产的xml的布局

        drawlayout_second = view.findViewById(R.id.drawlayout_second);//找到侧滑汉堡菜单的布局
        change_state_unlog();//登录和注册按钮动态生成

        /**
         * 定位相关
         */
        mLocationClient = new LocationClient(getContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true

        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明

        show_location = view.findViewById(R.id.location);
        mLocationClient.start();//调用LocationClient的start()方法，发起请求，在BDAbstractLocationListener接口中，便可获得定位地址相关的信息。
        dblocation = new BDLocation();
        myListener.onReceiveLocation(dblocation);
        show_location.setText(dblocation.getCountry());
        /**
         * 定位相关
         */


        up_btn = view.findViewById(R.id.up_btn);
        down_btn = view.findViewById(R.id.down_btn);

        listview = view.findViewById(R.id.list_view_menu);//找到添加侧滑菜单对应的控件

        home_menu = new String[]{"消息中心", "个性皮肤", "同步", "汇率计算器", "房贷计算器", "帮助与反馈", "关于", "清空缓存", "设置"};
        adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_expandable_list_item_1, home_menu);//创建适配器,并加载对应的布局资源

        listview.setAdapter(adapter);//listview与适配器的绑定

        show_list_btn = view.findViewById(R.id.show_list);//找到点击显示侧滑菜单的按钮
        drawerLayout = view.findViewById(R.id.drawerlayout);//找到要显示的侧滑菜单的根布局
        show_list_btn.setOnClickListener(new View.OnClickListener() {//编写按钮的点击显示侧滑菜单的事件
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START);//openDrawer()函数用来打开侧滑菜单,Gravity.START表示布局文件里设置的滑出方向
            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {//对listview里的item设置监听事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id == 4) {
                    Intent intent = new Intent(getContext(), Home_house_Activity.class);
                    startActivity(intent);
                }
                if (id == 3) {

                    mDialog = new Dialog(getContext());
                    mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    View v = inflater.inflate(R.layout.dialog_exchange_rage, null);//弹窗视图在这里
                    mDialog.setContentView(v);
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

                    Window window = mDialog.getWindow();
                    lp.copyFrom(window.getAttributes());
                    lp.width = 1000;
                    lp.height = 1100;
                    lp.gravity = Gravity.CENTER;
                    mDialog.show();
                    window.setAttributes(lp);

                    up = v.findViewById(R.id.up_edit);
                    down = v.findViewById(R.id.down_edit);
                    up.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            //在这里编写改变上面输入框，下面输入框的实时监听
                            down_string = down.getHint().toString();//先获取下面输入的hint值，用于计算
                            up_string = up.getText().toString();//获取上面输入框的输入内容
                            if (!TextUtils.isEmpty(up_string) && !TextUtils.isEmpty(down_string)) {
                                up_double = Double.parseDouble(up_string);//如果不为空则再一次类型转换
                                down_double = Double.parseDouble(down_string);//将获取下面的hint值转换为double型
                                result = up_double * down_double;
                                down.setText(format("%.2f", result) + "");//保留两位小数
                            } else down.setText("");//如果为空，就显示初始值
                        }
                    });
                    down.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                    up_btn = v.findViewById(R.id.up_btn);//找到上面的图案按钮并设点击事件
                    up_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent_up = new Intent(getContext(), Exchange_rate_Activity_up.class);
                            //用startActivityForResult启动点击活动
                            startActivityForResult(intent_up, SELECT_MONEY_UP);
                        }
                    });
                    down_btn = v.findViewById(R.id.down_btn);//找到下面的图案按钮并设点击事件
                    down_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent_down = new Intent(getContext(), Exchange_rate_Activity_down.class);
                            startActivityForResult(intent_down, SELECT_MONEY_DOWN);
                        }
                    });

                }
            }
        });

        //动态添加资产函数测试(测试成功)
        //add_asset(new AssetAccount());
        own_asset = view.findViewById(R.id.own_asset);

        /**
         * 异步取数据库中的数据
         */
        LitePal.findAllAsync(AssetAccount.class).listen(new FindMultiCallback<AssetAccount>() {
            @Override
            public void onFinish(List<AssetAccount> list) {
                list = LitePal.findAll(AssetAccount.class);//找到所有数据
                add_asset(list);
            }
        });

        all_asset = view.findViewById(R.id.all_asset);//找到总资产文字显示框

        return view;//返回具体的布局
    }//oncreate结束


    private void change_state_unlog() {

        //头像图片编写
        ImageView head_icon = new ImageView(getContext());
        head_icon.setBackgroundResource(R.drawable.head_icon);
        LinearLayout.LayoutParams head_icon_Params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        head_icon_Params.weight = 1;//设置权重
        head_icon.setLayoutParams(head_icon_Params);
        //头像图片编写完毕

        //登录按钮编写
        Button login = new Button(getContext());
        login.setBackgroundResource(R.drawable.line_block);
        login.setText("登录");
        login.setTextSize(16);
        login.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);//加粗
        LinearLayout.LayoutParams login_Params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        login_Params.weight = 1;//设置权重
        login.setLayoutParams(login_Params);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Home_login_Activity.class);
                startActivityForResult(intent,TOLOG);
            }
        });
        //登录按钮编写完毕

        //注册按钮编写
        Button register = new Button(getContext());
        register.setBackgroundResource(R.drawable.line_block);
        register.setText("注册");
        register.setTextSize(16);
        register.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);//加粗
        LinearLayout.LayoutParams register_Params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        register_Params.weight = 1;//设置权重
        register.setLayoutParams(register_Params);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Home_register_Activity.class);
                startActivity(intent);
            }
        });
        //注册按钮编写完毕

        drawlayout_second.addView(head_icon);//将头像图片加入布局
        drawlayout_second.addView(login);//将登录按钮加入布局
        drawlayout_second.addView(register);//将注册按钮加入布局
    }

    private void change_state_haveloged() {


        //头像图片编写
        ImageView head_icon = new ImageView(getContext());
        head_icon.setBackgroundResource(R.drawable.head_icon);
        LinearLayout.LayoutParams head_icon_Params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        head_icon_Params.weight = 1;//设置权重
        head_icon.setLayoutParams(head_icon_Params);
        //头像图片编写完毕


        //用户名TextView编写
        TextView user_name = new TextView(getContext());
        user_name.setText(Home_login_Activity.search_name);//用static偷机取巧的做法！
        user_name.setTextSize(16);
        user_name.setTextColor(Color.BLACK);//黑色字体
        user_name.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);//加粗
        LinearLayout.LayoutParams user_name_Params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        user_name_Params.setMargins(0,40,0,0);
        user_name_Params.weight = 1;//设置权重
        user_name.setLayoutParams(user_name_Params);
        //用户名TextView编写完毕

        //注册按钮编写
        Button sign_out = new Button(getContext());
        sign_out.setBackgroundResource(R.drawable.line_block);
        sign_out.setText("退出");
        sign_out.setTextSize(16);
        sign_out.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);//加粗
        LinearLayout.LayoutParams sign_out_Params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        sign_out_Params.weight = 1;//设置权重
        sign_out.setLayoutParams(sign_out_Params);
        sign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawlayout_second.removeAllViews();
                change_state_unlog();//回到未登录状态
            }
        });
        //注册按钮编写完毕

        drawlayout_second.addView(head_icon);//将头像图片加入布局
        drawlayout_second.addView(user_name);//将登录按钮加入布局
        drawlayout_second.addView(sign_out);//将注册按钮加入布局
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    //动态生成资产列表函数函数编写
    public void add_asset(List<AssetAccount> list) {//关键在于add_asset()里的参数！！！
//        SqlAll sqlAll = new SqlAll();
        double own_asset_number = 0;//净资产初始化
        double all_asset_number = 0;//总资产初始化
        for (int i = 0; i < list.size(); i++) {
            get_more_message = list.get(i).getA_cardType();//遍历获取备注信息（就是卡类别）
            get_balance = list.get(i).getA_balance();//遍历获取资产余额
            all_asset_number += get_balance;//总资产叠加
            own_asset_number += get_balance;//净资产叠加
            own_asset.setText("净资产￥:" + format("%.2f", own_asset_number) + "");//更改净资产,并设置保留两位小数
            all_asset.setText("总资产￥:" + format("%.2f", all_asset_number) + "");//更改总资产,并设置保留两位小数
            get_bank_name = list.get(i).getA_bankName();//遍历获取银行名称
            // Log.d(TAG, list.size()+"add_asset: ok159");
            LinearLayout twice = new LinearLayout(getContext());//动态创建第二层的布局
            //设置第二层布局的大小等属性
            LinearLayout.LayoutParams twice_Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 160);
            twice_Params.setMargins(28, 10, 28, 0);//设置边距
            twice.setBackgroundResource(R.drawable.line_block);//第二层布局设置为白色带边框背景
            twice.setLayoutParams(twice_Params);//将上面的大小和边距属性赋给第二层布局
            //第二层布局调整完毕

            ImageView money_from = new ImageView(getContext());//创建左边的图片
            //ImageView的自身布局
            RelativeLayout.LayoutParams money_from_layout = new RelativeLayout.LayoutParams(110, 110);
            money_from_layout.setMargins(24, 18, 0, 0);//通过设置ImageView的边距位置达到居中效果
            switch (get_bank_name) {
                case "交通银行":
                    money_from.setBackgroundResource(R.drawable.bcm);//给ImageView加上图片资源
                    break;
                case "工商银行":
                    money_from.setBackgroundResource(R.drawable.icbc);//给ImageView加上图片资源
                    break;
                case "招商银行":
                    money_from.setBackgroundResource(R.drawable.cmb);//给ImageView加上图片资源
                    break;
                case "中国银行":
                    money_from.setBackgroundResource(R.drawable.boc);//给ImageView加上图片资源
                    break;
                case "农业银行":
                    money_from.setBackgroundResource(R.drawable.abc);//给ImageView加上图片资源
                    break;
                case "浦发银行":
                    money_from.setBackgroundResource(R.drawable.spdb);//给ImageView加上图片资源
                    break;
                case "建设银行":
                    money_from.setBackgroundResource(R.drawable.ccb);//给ImageView加上图片资源
                    break;
                case "微信":
                    money_from.setBackgroundResource(R.drawable.wechat);//给ImageView加上图片资源
                    break;
                case "支付宝":
                    money_from.setBackgroundResource(R.drawable.alipay);//给ImageView加上图片资源
                    break;
            }
            money_from.setLayoutParams(money_from_layout);//将ImageView加入到自己的布局中
            twice.addView(money_from);//将ImageView加入到第二层布局中
            //ImageView编写完毕

            //第三层布局——垂直的放置资产来源以及备注信息
            LinearLayout third = new LinearLayout(getContext());//动态创建第三层的布局
            third.setOrientation(LinearLayout.VERTICAL);//设置为垂直的线性布局,因为默认为水平的
            //设置第二层布局的大小等属性
            LinearLayout.LayoutParams third_Params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            third_Params.setMargins(0, 0, 0, 0);//设置边距
            third.setBackgroundColor(getResources().getColor(R.color.color6));//第三层布局设置为透明背景
            third.setLayoutParams(third_Params);//将上面的大小和边距属性赋给第三层布局
            twice.addView(third);
            //第三层布局调整完毕

            TextView bank_name = new TextView(getContext());//创建一个TextView，用来存放银行名称
            //TextView的自身布局
            LinearLayout.LayoutParams bank_name_layout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            bank_name_layout.setMargins(29, 8, 0, 0);//通过设置ImageView的边距位置达到居中效果
            bank_name.setText(get_bank_name);//添加字体
            bank_name.setTextSize(19);
            bank_name.setTextColor(Color.BLACK);//字体设为黑色
            bank_name.setLayoutParams(bank_name_layout);//将bank_name的机身布局放入
            third.addView(bank_name);//将bank_name加入到第三层布局中
            //银行名称TextView编写完毕

            TextView more_message = new TextView(getContext());//创建一个TextView，用来存放备注信息
            //TextView的自身布局
            LinearLayout.LayoutParams more_message_layout = new LinearLayout.LayoutParams(230, ViewGroup.LayoutParams.WRAP_CONTENT);
            more_message_layout.setMargins(29, 5, 0, 0);//通过设置ImageView的边距位置达到居中效果
            more_message.setText(get_more_message);//添加备注信息字体
            more_message.setTextSize(15);
            more_message.setTextColor(getResources().getColor(R.color.color5));//字体设为#666666的灰色
            more_message.setLayoutParams(more_message_layout);//将备注信息的自身布局放入
            third.addView(more_message);//将bank_name加入到第三层布局中
            //备注信息TextView编写完毕

            TextView balance = new TextView(getContext());//创建一个TextView，用来存放余额
            //TextView的自身布局
            RelativeLayout.LayoutParams balance_layout = new RelativeLayout.LayoutParams(350, 90);
            balance_layout.setMargins(338, 37, 0, 0);//通过设置边距位置达到居中效果
            balance.setText("￥" + get_balance);//添加字体
            balance.setTextSize(20);
            balance.setTextColor(Color.BLACK);//字体设为黑色
            balance.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);//设置字体加粗(网上找的这行代码)
            balance.setLayoutParams(balance_layout);//将余额的自身布局放入
            twice.addView(balance);//将余额加入到第二层布局中
            //金额TextView编写完毕

            first_layout.addView(twice);//将第二层布局加入到第一层布局中
        }
    }

    @Override
    public void onActivityResult(int requesstCode, int resultCode, Intent data) {
        super.onActivityResult(requesstCode, resultCode, data);
        switch (requesstCode) {
            case TOLOG:
                if (resultCode == RESULT_OK){
                    //移除动态生成的登录和注册按钮控件，并变为新的用户名
                    drawlayout_second.removeAllViews();
                    change_state_haveloged();//变为登录模式
                    Log.d("753", "onActivityResult: 移除动态生成的登录和注册按钮控件，并变为新的用户名");
                }
                break;
            case SELECT_MONEY_UP://上面的图标点击发生
                switch (resultCode) {
                    //对应的结果码,先改变图片，再设置hint
                    case 0:
                        up_btn.setBackgroundResource(R.drawable.cny_circle);
                        up.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        //判断下面的币种
                        switch (image_down()) {
                            case 1:
                                down.setHint("1.00");
                                break;
                            case 2:
                                down.setHint("0.13");
                                break;
                            case 3:
                                down.setHint("4.58");
                                break;
                            case 4:
                                down.setHint("0.21");
                                break;
                            case 5:
                                down.setHint("0.57");
                                break;
                            case 6:
                                down.setHint("0.19");
                                break;
                            case 7:
                                down.setHint("0.96");
                                break;
                            case 8:
                                down.setHint("0.11");
                                break;
                            case 9:
                                down.setHint("1.13");
                                break;
                            case 10:
                                down.setHint("10.06");
                                break;
                            case 11:
                                down.setHint("584.45");
                                break;
                            case 12:
                                down.setHint("1.16");
                                break;
                            case 13:
                                down.setHint("0.22");
                                break;
                            case 14:
                                down.setHint("1.38");
                                break;
                            case 15:
                                down.setHint("0.14");
                                break;
                            case 16:
                                down.setHint("3367.00");
                                break;
                        }
                        break;
                    case 1:
                        up_btn.setBackgroundResource(R.drawable.eur_circle);
                        up.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        //判断下面的币种
                        switch (image_down()) {
                            case 1:
                                down.setHint("7.72");
                                break;
                            case 2:
                                down.setHint("1.00");
                                break;
                            case 3:
                                down.setHint("35.38");
                                break;
                            case 4:
                                down.setHint("1.61");
                                break;
                            case 5:
                                down.setHint("4.42");
                                break;
                            case 6:
                                down.setHint("1.51");
                                break;
                            case 7:
                                down.setHint("7.44");
                                break;
                            case 8:
                                down.setHint("0.88");
                                break;
                            case 9:
                                down.setHint("8.76");
                                break;
                            case 10:
                                down.setHint("77.81");
                                break;
                            case 11:
                                down.setHint("4520.16");
                                break;
                            case 12:
                                down.setHint("8.97");
                                break;
                            case 13:
                                down.setHint("1.71");
                                break;
                            case 14:
                                down.setHint("10.65");
                                break;
                            case 15:
                                down.setHint("1.12");
                                break;
                            case 16:
                                down.setHint("26040.40");
                                break;
                        }
                        break;
                    case 2:
                        up_btn.setBackgroundResource(R.drawable.thb_circle);
                        up.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_down()) {
                            case 1:
                                down.setHint("0.22");
                                break;
                            case 2:
                                down.setHint("0.03");
                                break;
                            case 3:
                                down.setHint("1.00");
                                break;
                            case 4:
                                down.setHint("0.05");
                                break;
                            case 5:
                                down.setHint("0.12");
                                break;
                            case 6:
                                down.setHint("0.04");
                                break;
                            case 7:
                                down.setHint("0.21");
                                break;
                            case 8:
                                down.setHint("0.02");
                                break;
                            case 9:
                                down.setHint("0.25");
                                break;
                            case 10:
                                down.setHint("2.2");
                                break;
                            case 11:
                                down.setHint("127.55");
                                break;
                            case 12:
                                down.setHint("0.25");
                                break;
                            case 13:
                                down.setHint("0.05");
                                break;
                            case 14:
                                down.setHint("0.3");
                                break;
                            case 15:
                                down.setHint("0.03");
                                break;
                            case 16:
                                down.setHint("734.82");
                                break;
                        }
                        break;
                    case 3:
                        up_btn.setBackgroundResource(R.drawable.aud_circle);
                        up.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_down()) {
                            case 1:
                                down.setHint("4.8");
                                break;
                            case 2:
                                down.setHint("0.62");
                                break;
                            case 3:
                                down.setHint("21.99");
                                break;
                            case 4:
                                down.setHint("1.00");
                                break;
                            case 5:
                                down.setHint("2.75");
                                break;
                            case 6:
                                down.setHint("0.94");
                                break;
                            case 7:
                                down.setHint("4.63");
                                break;
                            case 8:
                                down.setHint("0.55");
                                break;
                            case 9:
                                down.setHint("5.44");
                                break;
                            case 10:
                                down.setHint("48.34");
                                break;
                            case 11:
                                down.setHint("2808.36");
                                break;
                            case 12:
                                down.setHint("5.57");
                                break;
                            case 13:
                                down.setHint("1.06");
                                break;
                            case 14:
                                down.setHint("6.62");
                                break;
                            case 15:
                                down.setHint("0.69");
                                break;
                            case 16:
                                down.setHint("16178.79");
                                break;
                        }
                        break;
                    case 4:
                        up_btn.setBackgroundResource(R.drawable.brl_circle);
                        up.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_down()) {
                            case 1:
                                down.setHint("1.75");
                                break;
                            case 2:
                                down.setHint("0.23");
                                break;
                            case 3:
                                down.setHint("8.01");
                                break;
                            case 4:
                                down.setHint("0.36");
                                break;
                            case 5:
                                down.setHint("1.00");
                                break;
                            case 6:
                                down.setHint("0.34");
                                break;
                            case 7:
                                down.setHint("1.69");
                                break;
                            case 8:
                                down.setHint("0.2");
                                break;
                            case 9:
                                down.setHint("1.98");
                                break;
                            case 10:
                                down.setHint("17.59");
                                break;
                            case 11:
                                down.setHint("1021.79");
                                break;
                            case 12:
                                down.setHint("2.04");
                                break;
                            case 13:
                                down.setHint("0.39");
                                break;
                            case 14:
                                down.setHint("2.4");
                                break;
                            case 15:
                                down.setHint("0.25");
                                break;
                            case 16:
                                down.setHint("5886.45");
                                break;
                        }
                        break;
                    case 5:
                        up_btn.setBackgroundResource(R.drawable.cad_circle);
                        up.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_down()) {
                            case 1:
                                down.setHint("5.13");
                                break;
                            case 2:
                                down.setHint("0.66");
                                break;
                            case 3:
                                down.setHint("23.51");
                                break;
                            case 4:
                                down.setHint("1.07");
                                break;
                            case 5:
                                down.setHint("2.94");
                                break;
                            case 6:
                                down.setHint("1.00");
                                break;
                            case 7:
                                down.setHint("4.95");
                                break;
                            case 8:
                                down.setHint("0.59");
                                break;
                            case 9:
                                down.setHint("5.82");
                                break;
                            case 10:
                                down.setHint("51.67");
                                break;
                            case 11:
                                down.setHint("3001.81");
                                break;
                            case 12:
                                down.setHint("5.96");
                                break;
                            case 13:
                                down.setHint("1.14");
                                break;
                            case 14:
                                down.setHint("7.08");
                                break;
                            case 15:
                                down.setHint("0.74");
                                break;
                            case 16:
                                down.setHint("17293.27");
                                break;
                        }
                        break;
                    case 6:
                        up_btn.setBackgroundResource(R.drawable.dkk_circle);
                        up.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_down()) {
                            case 1:
                                down.setHint("1.04");
                                break;
                            case 2:
                                down.setHint("0.13");
                                break;
                            case 3:
                                down.setHint("4.75");
                                break;
                            case 4:
                                down.setHint("0.22");
                                break;
                            case 5:
                                down.setHint("0.59");
                                break;
                            case 6:
                                down.setHint("0.2");
                                break;
                            case 7:
                                down.setHint("1.00");
                                break;
                            case 8:
                                down.setHint("0.12");
                                break;
                            case 9:
                                down.setHint("1.18");
                                break;
                            case 10:
                                down.setHint("10.42");
                                break;
                            case 11:
                                down.setHint("605.2");
                                break;
                            case 12:
                                down.setHint("1.2");
                                break;
                            case 13:
                                down.setHint("0.23");
                                break;
                            case 14:
                                down.setHint("1.43");
                                break;
                            case 15:
                                down.setHint("0.15");
                                break;
                            case 16:
                                down.setHint("3486.53");
                                break;
                        }
                        break;
                    case 7:
                        up_btn.setBackgroundResource(R.drawable.gbp_circle);
                        up.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_down()) {
                            case 1:
                                down.setHint("8.75");
                                break;
                            case 2:
                                down.setHint("1.13");
                                break;
                            case 3:
                                down.setHint("40.11");
                                break;
                            case 4:
                                down.setHint("1.82");
                                break;
                            case 5:
                                down.setHint("5.01");
                                break;
                            case 6:
                                down.setHint("1.71");
                                break;
                            case 7:
                                down.setHint("8.44");
                                break;
                            case 8:
                                down.setHint("1.00");
                                break;
                            case 9:
                                down.setHint("9.93");
                                break;
                            case 10:
                                down.setHint("88.18");
                                break;
                            case 11:
                                down.setHint("5122.62");
                                break;
                            case 12:
                                down.setHint("10.17");
                                break;
                            case 13:
                                down.setHint("1.94");
                                break;
                            case 14:
                                down.setHint("12.07");
                                break;
                            case 15:
                                down.setHint("1.26");
                                break;
                            case 16:
                                down.setHint("29511.11");
                                break;
                        }
                        break;
                    case 8:
                        up_btn.setBackgroundResource(R.drawable.hkd_circle);
                        up.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_down()) {
                            case 1:
                                down.setHint("0.88");
                                break;
                            case 2:
                                down.setHint("0.11");
                                break;
                            case 3:
                                down.setHint("4.04");
                                break;
                            case 4:
                                down.setHint("0.18");
                                break;
                            case 5:
                                down.setHint("0.5");
                                break;
                            case 6:
                                down.setHint("0.17");
                                break;
                            case 7:
                                down.setHint("0.85");
                                break;
                            case 8:
                                down.setHint("0.1");
                                break;
                            case 9:
                                down.setHint("1.00");
                                break;
                            case 10:
                                down.setHint("8.88");
                                break;
                            case 11:
                                down.setHint("515.9");
                                break;
                            case 12:
                                down.setHint("1.02");
                                break;
                            case 13:
                                down.setHint("0.2");
                                break;
                            case 14:
                                down.setHint("1.22");
                                break;
                            case 15:
                                down.setHint("0.13");
                                break;
                            case 16:
                                down.setHint("2972.05");
                                break;
                        }
                        break;
                    case 9:
                        up_btn.setBackgroundResource(R.drawable.inr_circle);
                        up.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_down()) {
                            case 1:
                                down.setHint("0.1");
                                break;
                            case 2:
                                down.setHint("0.01");
                                break;
                            case 3:
                                down.setHint("0.46");
                                break;
                            case 4:
                                down.setHint("0.02");
                                break;
                            case 5:
                                down.setHint("0.06");
                                break;
                            case 6:
                                down.setHint("0.02");
                                break;
                            case 7:
                                down.setHint("0.1");
                                break;
                            case 8:
                                down.setHint("0.01");
                                break;
                            case 9:
                                down.setHint("0.11");
                                break;
                            case 10:
                                down.setHint("1.00");
                                break;
                            case 11:
                                down.setHint("58.09");
                                break;
                            case 12:
                                down.setHint("0.12");
                                break;
                            case 13:
                                down.setHint("0.02");
                                break;
                            case 14:
                                down.setHint("0.14");
                                break;
                            case 15:
                                down.setHint("0.01");
                                break;
                            case 16:
                                down.setHint("334.67");
                                break;
                        }
                        break;
                    case 10:
                        up_btn.setBackgroundResource(R.drawable.khr_circle);
                        up.setHint("1000.00");
                        down.setText("");
                        up.setText("");
                        switch (image_down()) {
                            case 1:
                                down.setHint("1.71");
                                break;
                            case 2:
                                down.setHint("0.22");
                                break;
                            case 3:
                                down.setHint("7.84");
                                break;
                            case 4:
                                down.setHint("0.36");
                                break;
                            case 5:
                                down.setHint("0.98");
                                break;
                            case 6:
                                down.setHint("0.33");
                                break;
                            case 7:
                                down.setHint("1.65");
                                break;
                            case 8:
                                down.setHint("0.2");
                                break;
                            case 9:
                                down.setHint("1.94");
                                break;
                            case 10:
                                down.setHint("17.21");
                                break;
                            case 11:
                                down.setHint("1.00");
                                break;
                            case 12:
                                down.setHint("2.00");
                                break;
                            case 13:
                                down.setHint("0.38");
                                break;
                            case 14:
                                down.setHint("2.35");
                                break;
                            case 15:
                                down.setHint("0.25");
                                break;
                            case 16:
                                down.setHint("5760.94");
                                break;
                        }
                        break;
                    case 11:
                        up_btn.setBackgroundResource(R.drawable.mop_circle);
                        up.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_down()) {
                            case 1:
                                down.setHint("0.86");
                                break;
                            case 2:
                                down.setHint("0.11");
                                break;
                            case 3:
                                down.setHint("3.94");
                                break;
                            case 4:
                                down.setHint("0.18");
                                break;
                            case 5:
                                down.setHint("0.49");
                                break;
                            case 6:
                                down.setHint("0.17");
                                break;
                            case 7:
                                down.setHint("0.83");
                                break;
                            case 8:
                                down.setHint("0.1");
                                break;
                            case 9:
                                down.setHint("0.98");
                                break;
                            case 10:
                                down.setHint("8.62");
                                break;
                            case 11:
                                down.setHint("500.82");
                                break;
                            case 12:
                                down.setHint("1.00");
                                break;
                            case 13:
                                down.setHint("0.19");
                                break;
                            case 14:
                                down.setHint("1.19");
                                break;
                            case 15:
                                down.setHint("0.12");
                                break;
                            case 16:
                                down.setHint("2885.19");
                                break;
                        }
                        break;
                    case 12:
                        up_btn.setBackgroundResource(R.drawable.nzd_circle);
                        up.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_down()) {
                            case 1:
                                down.setHint("4.51");
                                break;
                            case 2:
                                down.setHint("0.58");
                                break;
                            case 3:
                                down.setHint("20.69");
                                break;
                            case 4:
                                down.setHint("0.94");
                                break;
                            case 5:
                                down.setHint("2.59");
                                break;
                            case 6:
                                down.setHint("0.88");
                                break;
                            case 7:
                                down.setHint("4.35");
                                break;
                            case 8:
                                down.setHint("0.52");
                                break;
                            case 9:
                                down.setHint("5.12");
                                break;
                            case 10:
                                down.setHint("45.47");
                                break;
                            case 11:
                                down.setHint("2641.38");
                                break;
                            case 12:
                                down.setHint("5.25");
                                break;
                            case 13:
                                down.setHint("1.00");
                                break;
                            case 14:
                                down.setHint("6.23");
                                break;
                            case 15:
                                down.setHint("0.65");
                                break;
                            case 16:
                                down.setHint("15216.84");
                                break;
                        }
                        break;
                    case 13:
                        up_btn.setBackgroundResource(R.drawable.sek_circle);
                        up.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_down()) {
                            case 1:
                                down.setHint("0.72");
                                break;
                            case 2:
                                down.setHint("0.09");
                                break;
                            case 3:
                                down.setHint("3.32");
                                break;
                            case 4:
                                down.setHint("0.15");
                                break;
                            case 5:
                                down.setHint("0.42");
                                break;
                            case 6:
                                down.setHint("0.14");
                                break;
                            case 7:
                                down.setHint("0.7");
                                break;
                            case 8:
                                down.setHint("0.08");
                                break;
                            case 9:
                                down.setHint("0.82");
                                break;
                            case 10:
                                down.setHint("7.32");
                                break;
                            case 11:
                                down.setHint("425.01");
                                break;
                            case 12:
                                down.setHint("0.84");
                                break;
                            case 13:
                                down.setHint("0.16");
                                break;
                            case 14:
                                down.setHint("1.00");
                                break;
                            case 15:
                                down.setHint("0.1");
                                break;
                            case 16:
                                down.setHint("2448.48");
                                break;
                        }
                        break;
                    case 14:
                        up_btn.setBackgroundResource(R.drawable.usd_circle);
                        up.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_down()) {
                            case 1:
                                down.setHint("6.92");
                                break;
                            case 2:
                                down.setHint("0.9");
                                break;
                            case 3:
                                down.setHint("31.73");
                                break;
                            case 4:
                                down.setHint("1.44");
                                break;
                            case 5:
                                down.setHint("3.96");
                                break;
                            case 6:
                                down.setHint("1.35");
                                break;
                            case 7:
                                down.setHint("6.67");
                                break;
                            case 8:
                                down.setHint("0.79");
                                break;
                            case 9:
                                down.setHint("7.85");
                                break;
                            case 10:
                                down.setHint("69.72");
                                break;
                            case 11:
                                down.setHint("4050.26");
                                break;
                            case 12:
                                down.setHint("8.04");
                                break;
                            case 13:
                                down.setHint("1.53");
                                break;
                            case 14:
                                down.setHint("9.55");
                                break;
                            case 15:
                                down.setHint("1.00");
                                break;
                            case 16:
                                down.setHint("23333.33");
                                break;
                        }
                        break;
                    case 15:
                        up_btn.setBackgroundResource(R.drawable.vnd_circle);
                        up.setHint("1000");
                        down.setText("");
                        up.setText("");
                        switch (image_down()) {
                            case 1:
                                down.setHint("0.3");
                                break;
                            case 2:
                                down.setHint("0.04");
                                break;
                            case 3:
                                down.setHint("1.36");
                                break;
                            case 4:
                                down.setHint("0.06");
                                break;
                            case 5:
                                down.setHint("0.17");
                                break;
                            case 6:
                                down.setHint("0.06");
                                break;
                            case 7:
                                down.setHint("0.29");
                                break;
                            case 8:
                                down.setHint("0.03");
                                break;
                            case 9:
                                down.setHint("0.34");
                                break;
                            case 10:
                                down.setHint("2.99");
                                break;
                            case 11:
                                down.setHint("173.58");
                                break;
                            case 12:
                                down.setHint("0.35");
                                break;
                            case 13:
                                down.setHint("0.07");
                                break;
                            case 14:
                                down.setHint("0.41");
                                break;
                            case 15:
                                down.setHint("0.04");
                                break;
                            case 16:
                                down.setHint("1.00");
                                break;
                        }
                        break;
                }
                break;
            case SELECT_MONEY_DOWN://下面的图标点击发生
                switch (resultCode) {
                    case 0:
                        down_btn.setBackgroundResource(R.drawable.cny_circle);
                        down.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_up()) {
                            case 1:
                                up.setHint("1.00");
                                break;
                            case 2:
                                up.setHint("0.13");
                                break;
                            case 3:
                                up.setHint("4.58");
                                break;
                            case 4:
                                up.setHint("0.21");
                                break;
                            case 5:
                                up.setHint("0.57");
                                break;
                            case 6:
                                up.setHint("0.19");
                                break;
                            case 7:
                                up.setHint("0.96");
                                break;
                            case 8:
                                up.setHint("0.11");
                                break;
                            case 9:
                                up.setHint("1.13");
                                break;
                            case 10:
                                up.setHint("10.06");
                                break;
                            case 11:
                                up.setHint("584.45");
                                break;
                            case 12:
                                up.setHint("1.16");
                                break;
                            case 13:
                                up.setHint("0.22");
                                break;
                            case 14:
                                up.setHint("1.38");
                                break;
                            case 15:
                                up.setHint("0.14");
                                break;
                            case 16:
                                up.setHint("3367.00");
                                break;
                        }
                        break;
                    case 1:
                        down_btn.setBackgroundResource(R.drawable.eur_circle);
                        down.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_up()) {
                            case 1:
                                up.setHint("7.72");
                                break;
                            case 2:
                                up.setHint("1.00");
                                break;
                            case 3:
                                up.setHint("35.38");
                                break;
                            case 4:
                                up.setHint("1.61");
                                break;
                            case 5:
                                up.setHint("4.42");
                                break;
                            case 6:
                                up.setHint("1.51");
                                break;
                            case 7:
                                up.setHint("7.44");
                                break;
                            case 8:
                                up.setHint("0.88");
                                break;
                            case 9:
                                up.setHint("8.76");
                                break;
                            case 10:
                                up.setHint("77.81");
                                break;
                            case 11:
                                up.setHint("4520.16");
                                break;
                            case 12:
                                up.setHint("8.97");
                                break;
                            case 13:
                                up.setHint("1.71");
                                break;
                            case 14:
                                up.setHint("10.65");
                                break;
                            case 15:
                                up.setHint("1.12");
                                break;
                            case 16:
                                up.setHint("26040.40");
                                break;
                        }
                        break;
                    case 2:
                        down_btn.setBackgroundResource(R.drawable.thb_circle);
                        down.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_up()) {
                            case 1:
                                up.setHint("0.22");
                                break;
                            case 2:
                                up.setHint("0.03");
                                break;
                            case 3:
                                up.setHint("1.00");
                                break;
                            case 4:
                                up.setHint("0.05");
                                break;
                            case 5:
                                up.setHint("0.12");
                                break;
                            case 6:
                                up.setHint("0.04");
                                break;
                            case 7:
                                up.setHint("0.21");
                                break;
                            case 8:
                                up.setHint("0.02");
                                break;
                            case 9:
                                up.setHint("0.25");
                                break;
                            case 10:
                                up.setHint("2.2");
                                break;
                            case 11:
                                up.setHint("127.55");
                                break;
                            case 12:
                                up.setHint("0.25");
                                break;
                            case 13:
                                up.setHint("0.05");
                                break;
                            case 14:
                                up.setHint("0.3");
                                break;
                            case 15:
                                up.setHint("0.03");
                                break;
                            case 16:
                                up.setHint("734.82");
                                break;
                        }
                        break;
                    case 3:
                        down_btn.setBackgroundResource(R.drawable.aud_circle);
                        down.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_up()) {
                            case 1:
                                up.setHint("4.8");
                                break;
                            case 2:
                                up.setHint("0.62");
                                break;
                            case 3:
                                up.setHint("21.99");
                                break;
                            case 4:
                                up.setHint("1.00");
                                break;
                            case 5:
                                up.setHint("2.75");
                                break;
                            case 6:
                                up.setHint("0.94");
                                break;
                            case 7:
                                up.setHint("4.63");
                                break;
                            case 8:
                                up.setHint("0.55");
                                break;
                            case 9:
                                up.setHint("5.44");
                                break;
                            case 10:
                                up.setHint("48.34");
                                break;
                            case 11:
                                up.setHint("2808.36");
                                break;
                            case 12:
                                up.setHint("5.57");
                                break;
                            case 13:
                                up.setHint("1.06");
                                break;
                            case 14:
                                up.setHint("6.62");
                                break;
                            case 15:
                                up.setHint("0.69");
                                break;
                            case 16:
                                up.setHint("16178.79");
                                break;
                        }
                        break;
                    case 4:
                        down_btn.setBackgroundResource(R.drawable.brl_circle);
                        down.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_up()) {
                            case 1:
                                up.setHint("1.75");
                                break;
                            case 2:
                                up.setHint("0.23");
                                break;
                            case 3:
                                up.setHint("8.01");
                                break;
                            case 4:
                                up.setHint("0.36");
                                break;
                            case 5:
                                up.setHint("1.00");
                                break;
                            case 6:
                                up.setHint("0.34");
                                break;
                            case 7:
                                up.setHint("1.69");
                                break;
                            case 8:
                                up.setHint("0.2");
                                break;
                            case 9:
                                up.setHint("1.98");
                                break;
                            case 10:
                                up.setHint("17.59");
                                break;
                            case 11:
                                up.setHint("1021.79");
                                break;
                            case 12:
                                up.setHint("2.04");
                                break;
                            case 13:
                                up.setHint("0.39");
                                break;
                            case 14:
                                up.setHint("2.4");
                                break;
                            case 15:
                                up.setHint("0.25");
                                break;
                            case 16:
                                up.setHint("5886.45");
                                break;
                        }
                        break;
                    case 5:
                        down_btn.setBackgroundResource(R.drawable.cad_circle);
                        down.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_up()) {
                            case 1:
                                up.setHint("5.13");
                                break;
                            case 2:
                                up.setHint("0.66");
                                break;
                            case 3:
                                up.setHint("23.51");
                                break;
                            case 4:
                                up.setHint("1.07");
                                break;
                            case 5:
                                up.setHint("2.94");
                                break;
                            case 6:
                                up.setHint("1.00");
                                break;
                            case 7:
                                up.setHint("4.95");
                                break;
                            case 8:
                                up.setHint("0.59");
                                break;
                            case 9:
                                up.setHint("5.82");
                                break;
                            case 10:
                                up.setHint("51.67");
                                break;
                            case 11:
                                up.setHint("3001.81");
                                break;
                            case 12:
                                up.setHint("5.96");
                                break;
                            case 13:
                                up.setHint("1.14");
                                break;
                            case 14:
                                up.setHint("7.08");
                                break;
                            case 15:
                                up.setHint("0.74");
                                break;
                            case 16:
                                up.setHint("17293.27");
                                break;
                        }
                        break;
                    case 6:
                        down_btn.setBackgroundResource(R.drawable.dkk_circle);
                        down.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_up()) {
                            case 1:
                                up.setHint("1.04");
                                break;
                            case 2:
                                up.setHint("0.13");
                                break;
                            case 3:
                                up.setHint("4.75");
                                break;
                            case 4:
                                up.setHint("0.22");
                                break;
                            case 5:
                                up.setHint("0.59");
                                break;
                            case 6:
                                up.setHint("0.2");
                                break;
                            case 7:
                                up.setHint("1.00");
                                break;
                            case 8:
                                up.setHint("0.12");
                                break;
                            case 9:
                                up.setHint("1.18");
                                break;
                            case 10:
                                up.setHint("10.42");
                                break;
                            case 11:
                                up.setHint("605.2");
                                break;
                            case 12:
                                up.setHint("1.2");
                                break;
                            case 13:
                                up.setHint("0.23");
                                break;
                            case 14:
                                up.setHint("1.43");
                                break;
                            case 15:
                                up.setHint("0.15");
                                break;
                            case 16:
                                up.setHint("3486.53");
                                break;
                        }
                        break;
                    case 7:
                        down_btn.setBackgroundResource(R.drawable.gbp_circle);
                        down.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_up()) {
                            case 1:
                                up.setHint("8.75");
                                break;
                            case 2:
                                up.setHint("1.13");
                                break;
                            case 3:
                                up.setHint("40.11");
                                break;
                            case 4:
                                up.setHint("1.82");
                                break;
                            case 5:
                                up.setHint("5.01");
                                break;
                            case 6:
                                up.setHint("1.71");
                                break;
                            case 7:
                                up.setHint("8.44");
                                break;
                            case 8:
                                up.setHint("1.00");
                                break;
                            case 9:
                                up.setHint("9.93");
                                break;
                            case 10:
                                up.setHint("88.18");
                                break;
                            case 11:
                                up.setHint("5122.62");
                                break;
                            case 12:
                                up.setHint("10.17");
                                break;
                            case 13:
                                up.setHint("1.94");
                                break;
                            case 14:
                                up.setHint("12.07");
                                break;
                            case 15:
                                up.setHint("1.26");
                                break;
                            case 16:
                                up.setHint("29511.11");
                                break;
                        }
                        break;
                    case 8:
                        down_btn.setBackgroundResource(R.drawable.hkd_circle);
                        down.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_up()) {
                            case 1:
                                up.setHint("0.88");
                                break;
                            case 2:
                                up.setHint("0.11");
                                break;
                            case 3:
                                up.setHint("4.04");
                                break;
                            case 4:
                                up.setHint("0.18");
                                break;
                            case 5:
                                up.setHint("0.5");
                                break;
                            case 6:
                                up.setHint("0.17");
                                break;
                            case 7:
                                up.setHint("0.85");
                                break;
                            case 8:
                                up.setHint("0.1");
                                break;
                            case 9:
                                up.setHint("1.00");
                                break;
                            case 10:
                                up.setHint("8.88");
                                break;
                            case 11:
                                up.setHint("515.9");
                                break;
                            case 12:
                                up.setHint("1.02");
                                break;
                            case 13:
                                up.setHint("0.2");
                                break;
                            case 14:
                                up.setHint("1.22");
                                break;
                            case 15:
                                up.setHint("0.13");
                                break;
                            case 16:
                                up.setHint("2972.05");
                                break;
                        }
                        break;
                    case 9:
                        down_btn.setBackgroundResource(R.drawable.inr_circle);
                        down.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_up()) {
                            case 1:
                                up.setHint("0.1");
                                break;
                            case 2:
                                up.setHint("0.01");
                                break;
                            case 3:
                                up.setHint("0.46");
                                break;
                            case 4:
                                up.setHint("0.02");
                                break;
                            case 5:
                                up.setHint("0.06");
                                break;
                            case 6:
                                up.setHint("0.02");
                                break;
                            case 7:
                                up.setHint("0.1");
                                break;
                            case 8:
                                up.setHint("0.01");
                                break;
                            case 9:
                                up.setHint("0.11");
                                break;
                            case 10:
                                up.setHint("1.00");
                                break;
                            case 11:
                                up.setHint("58.09");
                                break;
                            case 12:
                                up.setHint("0.12");
                                break;
                            case 13:
                                up.setHint("0.02");
                                break;
                            case 14:
                                up.setHint("0.14");
                                break;
                            case 15:
                                up.setHint("0.01");
                                break;
                            case 16:
                                up.setHint("334.67");
                                break;
                        }
                        break;
                    case 10:
                        down_btn.setBackgroundResource(R.drawable.khr_circle);
                        down.setHint("1000.00");
                        down.setText("");
                        up.setText("");
                        switch (image_up()) {
                            case 1:
                                up.setHint("1.71");
                                break;
                            case 2:
                                up.setHint("0.22");
                                break;
                            case 3:
                                up.setHint("7.84");
                                break;
                            case 4:
                                up.setHint("0.36");
                                break;
                            case 5:
                                up.setHint("0.98");
                                break;
                            case 6:
                                up.setHint("0.33");
                                break;
                            case 7:
                                up.setHint("1.65");
                                break;
                            case 8:
                                up.setHint("0.2");
                                break;
                            case 9:
                                up.setHint("1.94");
                                break;
                            case 10:
                                up.setHint("17.21");
                                break;
                            case 11:
                                up.setHint("1.00");
                                break;
                            case 12:
                                up.setHint("2.00");
                                break;
                            case 13:
                                up.setHint("0.38");
                                break;
                            case 14:
                                up.setHint("2.35");
                                break;
                            case 15:
                                up.setHint("0.25");
                                break;
                            case 16:
                                up.setHint("5760.94");
                                break;
                        }
                        break;
                    case 11:
                        down_btn.setBackgroundResource(R.drawable.mop_circle);
                        down.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_up()) {
                            case 1:
                                up.setHint("0.86");
                                break;
                            case 2:
                                up.setHint("0.11");
                                break;
                            case 3:
                                up.setHint("3.94");
                                break;
                            case 4:
                                up.setHint("0.18");
                                break;
                            case 5:
                                up.setHint("0.49");
                                break;
                            case 6:
                                up.setHint("0.17");
                                break;
                            case 7:
                                up.setHint("0.83");
                                break;
                            case 8:
                                up.setHint("0.1");
                                break;
                            case 9:
                                up.setHint("0.98");
                                break;
                            case 10:
                                up.setHint("8.62");
                                break;
                            case 11:
                                up.setHint("500.82");
                                break;
                            case 12:
                                up.setHint("1.00");
                                break;
                            case 13:
                                up.setHint("0.19");
                                break;
                            case 14:
                                up.setHint("1.19");
                                break;
                            case 15:
                                up.setHint("0.12");
                                break;
                            case 16:
                                up.setHint("2885.19");
                                break;
                        }
                        break;
                    case 12:
                        down_btn.setBackgroundResource(R.drawable.nzd_circle);
                        down.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_up()) {
                            case 1:
                                up.setHint("4.51");
                                break;
                            case 2:
                                up.setHint("0.58");
                                break;
                            case 3:
                                up.setHint("20.69");
                                break;
                            case 4:
                                up.setHint("0.94");
                                break;
                            case 5:
                                up.setHint("2.59");
                                break;
                            case 6:
                                up.setHint("0.88");
                                break;
                            case 7:
                                up.setHint("4.35");
                                break;
                            case 8:
                                up.setHint("0.52");
                                break;
                            case 9:
                                up.setHint("5.12");
                                break;
                            case 10:
                                up.setHint("45.47");
                                break;
                            case 11:
                                up.setHint("2641.38");
                                break;
                            case 12:
                                up.setHint("5.25");
                                break;
                            case 13:
                                up.setHint("1.00");
                                break;
                            case 14:
                                up.setHint("6.23");
                                break;
                            case 15:
                                up.setHint("0.65");
                                break;
                            case 16:
                                up.setHint("15216.84");
                                break;
                        }
                        break;
                    case 13:
                        down_btn.setBackgroundResource(R.drawable.sek_circle);
                        down.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_up()) {
                            case 1:
                                up.setHint("0.72");
                                break;
                            case 2:
                                up.setHint("0.09");
                                break;
                            case 3:
                                up.setHint("3.32");
                                break;
                            case 4:
                                up.setHint("0.15");
                                break;
                            case 5:
                                up.setHint("0.42");
                                break;
                            case 6:
                                up.setHint("0.14");
                                break;
                            case 7:
                                up.setHint("0.7");
                                break;
                            case 8:
                                up.setHint("0.08");
                                break;
                            case 9:
                                up.setHint("0.82");
                                break;
                            case 10:
                                up.setHint("7.32");
                                break;
                            case 11:
                                up.setHint("425.01");
                                break;
                            case 12:
                                up.setHint("0.84");
                                break;
                            case 13:
                                up.setHint("0.16");
                                break;
                            case 14:
                                up.setHint("1.00");
                                break;
                            case 15:
                                up.setHint("0.1");
                                break;
                            case 16:
                                up.setHint("2448.48");
                                break;
                        }
                        break;
                    case 14:
                        down_btn.setBackgroundResource(R.drawable.usd_circle);
                        down.setHint("1.00");
                        down.setText("");
                        up.setText("");
                        switch (image_up()) {
                            case 1:
                                up.setHint("6.92");
                                break;
                            case 2:
                                up.setHint("0.9");
                                break;
                            case 3:
                                up.setHint("31.73");
                                break;
                            case 4:
                                up.setHint("1.44");
                                break;
                            case 5:
                                up.setHint("3.96");
                                break;
                            case 6:
                                up.setHint("1.35");
                                break;
                            case 7:
                                up.setHint("6.67");
                                break;
                            case 8:
                                up.setHint("0.79");
                                break;
                            case 9:
                                up.setHint("7.85");
                                break;
                            case 10:
                                up.setHint("69.72");
                                break;
                            case 11:
                                up.setHint("4050.26");
                                break;
                            case 12:
                                up.setHint("8.04");
                                break;
                            case 13:
                                up.setHint("1.53");
                                break;
                            case 14:
                                up.setHint("9.55");
                                break;
                            case 15:
                                up.setHint("1.00");
                                break;
                            case 16:
                                up.setHint("23333.33");
                                break;
                        }
                        break;
                    case 15:
                        down_btn.setBackgroundResource(R.drawable.vnd_circle);
                        down.setHint("1000");
                        down.setText("");
                        up.setText("");
                        switch (image_up()) {
                            case 1:
                                up.setHint("0.3");
                                break;
                            case 2:
                                up.setHint("0.04");
                                break;
                            case 3:
                                up.setHint("1.36");
                                break;
                            case 4:
                                up.setHint("0.06");
                                break;
                            case 5:
                                up.setHint("0.17");
                                break;
                            case 6:
                                up.setHint("0.06");
                                break;
                            case 7:
                                up.setHint("0.29");
                                break;
                            case 8:
                                up.setHint("0.03");
                                break;
                            case 9:
                                up.setHint("0.34");
                                break;
                            case 10:
                                up.setHint("2.99");
                                break;
                            case 11:
                                up.setHint("173.58");
                                break;
                            case 12:
                                up.setHint("0.35");
                                break;
                            case 13:
                                up.setHint("0.07");
                                break;
                            case 14:
                                up.setHint("0.41");
                                break;
                            case 15:
                                up.setHint("0.04");
                                break;
                            case 16:
                                up.setHint("1.00");
                                break;
                        }
                        break;
                }
        }
    }

    //写一个下面图片的判断
    public int image_down() {
        int i = 0;
        if (down_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.cny_circle).getConstantState())) {
            i = 1;
        } else if (down_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.eur_circle).getConstantState())) {
            i = 2;
        } else if (down_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.thb_circle).getConstantState())) {
            i = 3;
        } else if (down_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.aud_circle).getConstantState())) {
            i = 4;
        } else if (down_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.brl_circle).getConstantState())) {
            i = 5;
        } else if (down_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.cad_circle).getConstantState())) {
            i = 6;
        } else if (down_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.dkk_circle).getConstantState())) {
            i = 7;
        } else if (down_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.gbp_circle).getConstantState())) {
            i = 8;
        } else if (down_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.hkd_circle).getConstantState())) {
            i = 9;
        } else if (down_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.inr_circle).getConstantState())) {
            i = 10;
        } else if (down_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.khr_circle).getConstantState())) {
            i = 11;
        } else if (down_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.mop_circle).getConstantState())) {
            i = 12;
        } else if (down_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.nzd_circle).getConstantState())) {
            i = 13;
        } else if (down_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.sek_circle).getConstantState())) {
            i = 14;
        } else if (down_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.usd_circle).getConstantState())) {
            i = 15;
        } else if (down_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.vnd_circle).getConstantState())) {
            i = 16;
        }
        return i;
    }

    public int image_up() {
        int i = 0;
        if (up_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.cny_circle).getConstantState())) {
            i = 1;
        } else if (up_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.eur_circle).getConstantState())) {
            i = 2;
        } else if (up_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.thb_circle).getConstantState())) {
            i = 3;
        } else if (up_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.aud_circle).getConstantState())) {
            i = 4;
        } else if (up_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.brl_circle).getConstantState())) {
            i = 5;
        } else if (up_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.cad_circle).getConstantState())) {
            i = 6;
        } else if (up_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.dkk_circle).getConstantState())) {
            i = 7;
        } else if (up_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.gbp_circle).getConstantState())) {
            i = 8;
        } else if (up_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.hkd_circle).getConstantState())) {
            i = 9;
        } else if (up_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.inr_circle).getConstantState())) {
            i = 10;
        } else if (up_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.khr_circle).getConstantState())) {
            i = 11;
        } else if (up_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.mop_circle).getConstantState())) {
            i = 12;
        } else if (up_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.nzd_circle).getConstantState())) {
            i = 13;
        } else if (up_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.sek_circle).getConstantState())) {
            i = 14;
        } else if (up_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.usd_circle).getConstantState())) {
            i = 15;
        } else if (up_btn.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.vnd_circle).getConstantState())) {
            i = 16;
        }
        return i;
    }
}

