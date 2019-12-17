package com.example.terminal_work.litepal;

import android.support.annotation.ColorLong;
import android.util.Log;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * AssetAccount —— 资产账户表
 *
 * A_cardType       卡类别     非空
 * A_bankName       银行名称
 * A_cardNumber     账户卡号
 * A_balance        账户余额    非空
 *
 * A_tallyList     Tally与AssetAccount建立n对1关系
 */
public class AssetAccount extends LitePalSupport {
    private static final String TAG = "AssetAccount";

    @Column(nullable = false)
    private String A_cardType;

    private String A_bankName;

    private long A_cardNumber;

    @Column(nullable = false)
    private double A_balance;

    //Tally与AssetAccount建立n对1关系
    private List<Tally> A_tallyList = new ArrayList<>();

    public String getA_cardType() {
        Log.d(TAG, A_cardType + "getA_cardType: ok159");
        return A_cardType;
    }

    public void setA_cardType(String a_cardType) {
        A_cardType = a_cardType;
        Log.d(TAG, "setA_cardType: ok159");
    }

    public String getA_bankName() {
        Log.d(TAG, A_bankName + "getA_bankName: ok159");
        return A_bankName;
    }

    public void setA_bankName(String a_bankName) {
        A_bankName = a_bankName;
        Log.d(TAG, "setA_bankName: ok159");
    }

    public long getA_cardNumber() {
        Log.d(TAG, A_cardNumber + "getA_cardNumber: ok159");
        return A_cardNumber;
    }

    public void setA_cardNumber(long a_cardNumber) {
        A_cardNumber = a_cardNumber;
        Log.d(TAG, "setA_cardNumber: ok159");
    }

    public double getA_balance() {
        Log.d(TAG, A_balance+"getA_balance: ok159");
        return A_balance;
    }

    public void setA_balance(double a_balance) {
        A_balance = a_balance;
        Log.d(TAG, "setA_balance: ok159");
    }


    public List<Tally> getA_tallyList() {
        return A_tallyList;
    }

    public void setA_tallyList(List<Tally> a_tallyList) {
        A_tallyList = a_tallyList;
    }
}
