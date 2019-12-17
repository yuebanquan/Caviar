package com.example.terminal_work.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.terminal_work.R;

import java.util.Calendar;

public class Calendar_dialog_Fragment extends DialogFragment {//直接继承封装好的DialogFragment

    public static Calendar_dialog_Fragment getInstance(int type){//getInstance里的int参数就是想告诉电脑他想返回一个什么类型的弹窗，这个type在它的上下文里已经定义好
        Calendar_dialog_Fragment dialog=new Calendar_dialog_Fragment();
        Bundle bundle=new Bundle();//new一个Bundle用于下面的setArguemnts函数里传参
        bundle.putInt("Dialog_Type",type);
        dialog.setArguments(bundle);
        return dialog;
    }

    //上面那个public static Calendar_dialog_Fragment getInstance(int type)一但调用就会进入下面的Dialog onCreateDialog()函数

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        Calendar c1= Calendar.getInstance();//因为Calendar是单例，所以不能用new，只能用Instance()
        int year=c1.get(Calendar.YEAR);//获取当前年份
        int monthOfYear=c1.get(Calendar.MONTH);//获取当前月份
        int dayOfMonth=c1.get(Calendar.DAY_OF_MONTH);//获取当前日份


        Dialog dialog=null;//先声明一个空的Dialog,后面再给他实例化
        int dialog_type=getArguments().getInt("Dialog_Type");//调用上面的函数
        switch( dialog_type){//用switch case给后续作业留下接口，可以直接往里面套
            case BillFragment.BILL_DIALOG_TYPE_DATE:
                //返回一个DatePickerDialog(日期选择弹窗),参1为上下文,参2为回调函数
                return new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selected_year, int selected_month, int selected_day) {
                        Toast.makeText(getContext(),
                                selected_year+"年"+(selected_month+1)+"月"+selected_day+"日",Toast.LENGTH_SHORT).show();
                        //下面确定bill页面更改日期的TextView控件
                         TextView change_time_bill=getActivity().findViewById(R.id.time_selected);
                         change_time_bill.setText(selected_year+"年"+(selected_month+1)+"月"+selected_day+"日");//更改文字内容，显示选择的日期
                    }
                },year,monthOfYear,dayOfMonth);

            case TableFragment.TABLE_DIALOG_TYPE_DATE:
                return new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selected_year, int selected_month, int selected_day) {
                        Toast.makeText(getContext(),
                                selected_year+"年"+(selected_month+1)+"月"+selected_day+"日",Toast.LENGTH_SHORT).show();
                        //下面确定bill页面更改日期的TextView控件
                        TextView change_table_bill=getActivity().findViewById(R.id.table_time);
                        change_table_bill.setText(selected_year+"年"+(selected_month+1)+"月"+selected_day+"日");//更改文字内容，显示选择的日期
                    }
                },year,monthOfYear,dayOfMonth);
        }
        return dialog;

    }
}

