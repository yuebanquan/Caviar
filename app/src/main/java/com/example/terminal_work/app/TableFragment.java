package com.example.terminal_work.app;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.terminal_work.R;
import com.example.terminal_work.litepal.Tally;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.ArrayList;
import java.util.List;

public class TableFragment extends Fragment {

    private static final String TAG = "TableFragment";
    /**
     * TextView table_time  声明在table页面中需要选择日期的控件
     * Spinner spinner  选择收入还是支出的下拉列表spinner
     * List<String>list=new ArrayList<String>()  创建一个用来对spinner添加内容的list
     * com.github.mikephil.charting.charts.PieChart table_chart  饼状图
     * <p>
     * 数据库中取出来的数据：
     * sum_catering     餐饮
     * sum_travel;      旅行
     * sum_shopping;    购物
     * sum_traffic;     交通
     * sum_communications;  通讯
     * sum_medical;         医疗
     * sum_housing;     住房
     * sum_parenting;   育儿
     * sum_education;   文教
     * sum_recreation;  娱乐
     * sum_pet;         宠物
     * sum_life;        生活
     * <p>
     * C_out_sum        支出类总和
     */

    public static final int TABLE_DIALOG_TYPE_DATE = 2;
    private TextView table_time;//声明在table页面中需要选择时间的控件
    private Spinner spinner;
    List<String> list = new ArrayList<String>();//创建一个用来对spinner添加内容的list
    private com.github.mikephil.charting.charts.PieChart table_chart;//饼状图
    private LinearLayout table_majoy_layout;

    Double sum_catering;    //餐饮
    Double sum_travel;      //旅行
    Double sum_shopping;    //购物
    Double sum_traffic;     //交通
    Double sum_communications;  //通讯
    Double sum_medical;     //医疗
    Double sum_housing;     //住房
    Double sum_parenting;   //育儿
    Double sum_education;   //文教
    Double sum_recreation;  //娱乐
    Double sum_pet;         //宠物
    Double sum_life;        //生活
    Double sum_other;       //其他

    //类别总金额
    Double[] pay_kind_allmoney;

    //类别名字
    String[] pay_kind_name=new String[]{"餐饮","旅行","购物","交通","通讯","医疗","住房",
            "育儿","文教","娱乐","宠物","生活",};

    //类别图案
    private int[] kind_icon = {R.drawable.food, R.drawable.travel,
            R.drawable.shop, R.drawable.traffic,
            R.drawable.mobile, R.drawable.medical, R.drawable.housing,
            R.drawable.kids, R.drawable.book,R.drawable.happy, R.drawable.pet,
            R.drawable.life};


    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflater使将xml布局文件转换为视图的一个类,container表示在container里面显示这个视图
        View view = inflater.inflate(R.layout.table_page, container, false);

        spinner = view.findViewById(R.id.table_select);//找到控制是否弹出菜单的spinner
        table_time = view.findViewById(R.id.table_time);
        table_chart = view.findViewById(R.id.table_pie_chart);
        table_majoy_layout = view.findViewById(R.id.table_majoy_layout);

        //lisi数据源的加入以及适配器的绑定
        list.add("支出");
        list.add("收入");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.my_spinner_layout, list);//适配器创建
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);//控件绑定适配器

        //获取数据库数据
        dipDb();


        //监听事件的封装
        all_Listene_TableFragment();

        //饼状图加入数据函数
        pie_chart_data();


        /**
         * 异步取数据库中的数据,饼状图的显示与否设置,以及报表项的动态添加
         */
        LitePal.findAllAsync(Tally.class).listen(new FindMultiCallback<Tally>() {
            @Override
            public void onFinish(List<Tally> list) {
                list = LitePal.findAll(Tally.class);//找到所有数据
                add_table(list);
            }
        });

//        /**
//         * 取数据库文件
//         */
//        sum_catering = LitePal.where("classify_id == ?", "1").sum(Tally.class, "T_money", double.class);
//        Log.d(TAG, "onCreateView: sum_catering = " + sum_catering);

        return view;//返回具体的布局
    }/**
     *onCreateView()结束
     */


    /**
     * 取数据库数值
     * sum_catering     餐饮
     * sum_travel;      旅行
     * sum_shopping;    购物
     * sum_traffic;     交通
     * sum_communications;  通讯
     * sum_medical;         医疗
     * sum_housing;     住房
     * sum_parenting;   育儿
     * sum_education;   文教
     * sum_recreation;  娱乐
     * sum_pet;         宠物
     * sum_life;        生活
     */
    private void dipDb() {
        sum_catering = LitePal.where("id == ?", "1").sum(Tally.class, "T_money", double.class);
        Log.d(TAG, "onCreateView: sum_catering = " + sum_catering);
        sum_travel = LitePal.where("classify_id == ?", "2").sum(Tally.class, "T_money", double.class);
        Log.d(TAG, "onCreateView: sum_travel = " + sum_travel);
        sum_shopping = LitePal.where("classify_id == ?", "3").sum(Tally.class, "T_money", double.class);
        sum_traffic = LitePal.where("classify_id == ?", "4").sum(Tally.class, "T_money", double.class);
        sum_communications = LitePal.where("classify_id == ?", "5").sum(Tally.class, "T_money", double.class);
        sum_medical = LitePal.where("classify_id == ?", "6").sum(Tally.class, "T_money", double.class);
        sum_housing = LitePal.where("classify_id == ?", "7").sum(Tally.class, "T_money", double.class);
        sum_parenting = LitePal.where("classify_id == ?", "8").sum(Tally.class, "T_money", double.class);
        sum_education = LitePal.where("classify_id == ?", "9").sum(Tally.class, "T_money", double.class);
        sum_recreation = LitePal.where("classify_id == ?", "10").sum(Tally.class, "T_money", double.class);
        sum_pet = LitePal.where("classify_id == ?", "11").sum(Tally.class, "T_money", double.class);
        sum_life = LitePal.where("classify_id == ?", "12").sum(Tally.class, "T_money", double.class);
        sum_other =  sum_traffic
                + sum_communications + sum_medical + sum_housing
                + sum_parenting + sum_education + sum_recreation
                + sum_pet + sum_life;

        pay_kind_allmoney=new Double[]{sum_catering,sum_travel,sum_shopping,sum_traffic,
                sum_communications,sum_medical,sum_housing,sum_parenting,sum_education,
                sum_recreation,sum_pet,sum_life};
    }

    /**
     * 监听类函数
     */
    private void all_Listene_TableFragment() {
        //1：选择日期的点击事件：点击弹出日期选择框
        table_time.setOnClickListener(new View.OnClickListener() {//编写点击显示日期选择的事件
            @Override
            public void onClick(View v) {//这个点击事件应该弹出日期选择弹窗
                Calendar_dialog_Fragment calendar_dialog_fragment = new Calendar_dialog_Fragment().getInstance(TABLE_DIALOG_TYPE_DATE);
                if (calendar_dialog_fragment != null) {
                    calendar_dialog_fragment.show(getFragmentManager(), "选择日期");
                }
            }
        });
        //2：图表点击监听
        table_chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {//选中某个值的时候
                //e为点击的位置(x,y)
                Toast.makeText(getContext(), "点击了" + e, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {//当什么都补选的时候

            }
        });
    }

    /**
     * 饼状图的具体数据及样式编写
     */
    //饼状图加入数据函数
    private void pie_chart_data() {
        table_chart.setUsePercentValues(true);
        table_chart.getDescription().setEnabled(false);
        table_chart.setExtraOffsets(5, 10, 5, 5);

        table_chart.setDragDecelerationFrictionCoef(0.95f);
//        //设置中间的内容,只有当下面的setDrawHoleEnabled为true的时候才编写，否则不美观
//        table_chart.setCenterText("报表");
//        table_chart.setCenterTextSize(15);

        table_chart.setDrawHoleEnabled(false);//是否绘制饼状图中间的圆
        //table_chart.setHoleColor(Color.WHITE);//设置中间的颜色,因为上面为false，所以这一步不必要

        //table_chart.setTransparentCircleColor(Color.WHITE);//设置中间圆环的颜色

        //table_chart.setHoleRadius(58f);//饼状图中间的圆的半径大小
        table_chart.setTransparentCircleRadius(60);

        table_chart.setDrawCenterText(true);

        table_chart.setRotationAngle(0);
        // 触摸旋转
        table_chart.setRotationEnabled(true);//设置饼状图是否可以旋转(默认为true)
        table_chart.setHighlightPerTapEnabled(true);//设置旋转的时候点中的tab是否高亮(默认为true)



        //显示的数据源
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        entries.add(new PieEntry(sum_catering.intValue(), "餐饮"));
        entries.add(new PieEntry(sum_travel.intValue(), "旅行"));
        entries.add(new PieEntry(sum_shopping.intValue(), "购物"));
        entries.add(new PieEntry(sum_other.intValue(), "其他"));

        //设置数据
        setData(entries);
        Legend l = table_chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setTextSize(15);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // 输入标签样式
        table_chart.setEntryLabelColor(Color.BLACK);
        table_chart.setEntryLabelTextSize(13);
    }

    //设置数据
    private void setData(ArrayList<PieEntry> entries) {
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        //数据和颜色
        ArrayList<Integer> colors = new ArrayList<Integer>();
        //ColorTemplate可以更改每个区域的颜色
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(13);
        data.setValueTextColor(Color.BLACK);
        table_chart.setData(data);
        table_chart.highlightValues(null);
        //刷新
        table_chart.invalidate();
    }
    /**
     * 饼状图的具体数据及样式编写结束
     */


    /**
     * 动态生成报表项
     */
    private void add_table(List<Tally> list) {
        if (list.size() == 0) {
            //如果账单中还没有支出数据
            table_chart.setVisibility(View.GONE);//饼状图不显示
        } else {//如果有支出数据，开始遍历抓取数据生成报表项
            /**
             * 从数据库取数据
             */
            for (int i = 0; i < 12; i++) {//12个支出类


                //第二层布局编写
                LinearLayout the_second = new LinearLayout(getContext());
                LinearLayout.LayoutParams the_second_Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 160);
                the_second.setLayoutParams(the_second_Params);
                //第二层布局编写完毕

                //类别图标编写
                ImageView icon = new ImageView(getContext());
                icon.setBackgroundResource(kind_icon[i]);
                LinearLayout.LayoutParams icon_Params = new LinearLayout.LayoutParams(135, 135);
                icon_Params.setMargins(35, 15, 0, 0);
                icon.setLayoutParams(icon_Params);
                //类别图标编写完毕


                //类别名字编写
                TextView classify_name = new TextView(getContext());
                LinearLayout.LayoutParams classify_name_Params = new LinearLayout.LayoutParams(500, ViewGroup.LayoutParams.MATCH_PARENT);
                classify_name_Params.setMargins(20,60,0,0);
                classify_name.setText(pay_kind_name[i]);
                classify_name.setTextSize(18);
                classify_name.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);//加粗
                classify_name.setTextColor(getResources().getColor(R.color.color5));//设置文字颜色
                classify_name.setLayoutParams(classify_name_Params);
                //类别名字编写完毕


                //金额文字的编写
                TextView table_item_money = new TextView(getContext());
                table_item_money.setText("￥"+pay_kind_allmoney[i].toString());
                table_item_money.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);//加粗
                table_item_money.setTextSize(18);
                classify_name.setTextColor(getResources().getColor(R.color.color5));//设置文字颜色
                LinearLayout.LayoutParams table_item_money_Params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                table_item_money_Params.setMargins(20, 60, 0, 0);
                table_item_money.setLayoutParams(table_item_money_Params);
                //金额文字编写完毕


                the_second.addView(icon);//将类别图标装入总布局中
                the_second.addView(classify_name);//将类别名字放入第三层布局
                the_second.addView(table_item_money);
                table_majoy_layout.addView(the_second);//将第二层布局装入总布局中
            }
        }
    }

//    /**
//     * 计算支出各类所占比重
//     */
//    private void Cout_weight() {
//        C_out_sum = sum_catering + sum_travel + sum_shopping + sum_traffic
//                + sum_communications + sum_medical + sum_housing
//                + sum_parenting + sum_education + sum_recreation
//                + sum_pet + sum_life;
//        weight_catering = sum_catering / C_out_sum * 100;
//        Log.d(TAG, "Cout_weight: weight_catering = " + weight_catering.intValue());
//        weight_travel = sum_travel / C_out_sum * 100;
//        Log.d(TAG, "Cout_weight: weight_travel = " + weight_travel.intValue());
//        weight_shopping = sum_shopping / C_out_sum * 100;
//        Log.d(TAG, "Cout_weight: weight_shopping " + weight_shopping.intValue());
//        weight_other = 1 - weight_catering - weight_travel - weight_shopping * 100;
//        Log.d(TAG, "Cout_weight: weight_other " + weight_other.intValue());
//    }
}



