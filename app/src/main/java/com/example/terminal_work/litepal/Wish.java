package com.example.terminal_work.litepal;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * Wish —— 心愿表
 *
 * W_name           心愿名称  非空
 * W_money          目标金额  非空
 * W_date           记录日期  非空
 * W_month          需要月份  非空
 * W_location       地点
 */
public class Wish extends LitePalSupport {
    @Column(nullable = false)
    private String W_name;

    @Column(nullable = false)
    private double W_money;

    @Column(nullable = false)
    private Date W_date;

    @Column(nullable = false)
    private int W_month;

    private String W_location;

    public String getW_name() {
        return W_name;
    }

    public void setW_name(String w_name) {
        W_name = w_name;
    }

    public double getW_money() {
        return W_money;
    }

    public void setW_money(double w_money) {
        W_money = w_money;
    }

    public Date getW_date() {
        return W_date;
    }

    public void setW_date(Date w_date) {
        W_date = w_date;
    }

    public int getW_month() { return W_month; }

    public void setW_month(int w_month) { W_month = w_month; }

    public String getW_location() {
        return W_location;
    }

    public void setW_location(String w_location) {
        W_location = w_location;
    }
}
