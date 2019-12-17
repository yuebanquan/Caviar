package com.example.terminal_work.litepal;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.Date;


/**
 * Tally —— 记账表
 * <p>
 * T_comment    备注
 * T_money      金额      非空
 * T_Date       日期      非空
 * <p>
 * T_assetAccount     Tally与AssetAccount建立n对1关系
 * T_classify         Tally与classify建立n对1关系
 */
public class Tally extends LitePalSupport {

    private String T_comment;

    @Column(nullable = false)
    private double T_money;

    @Column(nullable = false)
    private Date T_Date;

    //Tally与AssetAccount建立n对1关系
    private AssetAccount T_assetAccount = new AssetAccount();

    //Tally与classify建立n对1关系
    private Classify T_classify = new Classify();

    public String getT_comment() {
        return T_comment;
    }

    public void setT_comment(String t_comment) {
        T_comment = t_comment;
    }

    public double getT_money() {
        return T_money;
    }

    public void setT_money(double t_money) {
        T_money = t_money;
    }


    public Date getT_Date() {
        return T_Date;
    }

    public void setT_Date(Date t_Date) {
        T_Date = t_Date;
    }

    public AssetAccount getT_assetAccount() {
        return T_assetAccount;
    }

    public void setT_assetAccount(AssetAccount t_assetAccount) {
        T_assetAccount = t_assetAccount;
    }

    public Classify getT_classify() {
        return T_classify;
    }

    public void setT_classify(Classify t_classify) {
        T_classify = t_classify;
    }
}
