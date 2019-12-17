package com.example.terminal_work.litepal;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 *Classify —— 记账分类表
 *
 * C_type   类别名称      非空
 * C_isIn   是否为流入    非空
 *
 * C_tallyList        Tally与classify建立n对1关系
 */
public class Classify extends LitePalSupport {
    @Column(nullable = false)
    private String C_type;      //类别名称

    @Column(nullable = false)
    private Boolean C_isIn;     //是否流入

    private List<Tally> C_tallyList= new ArrayList();

    public String getC_type() {
        return C_type;
    }

    public void setC_type(String c_type) {
        C_type = c_type;
    }

    public Boolean getC_isIn() {
        return C_isIn;
    }

    public void setC_isIn(Boolean c_isIn) {
        C_isIn = c_isIn;
    }

    public List<Tally> getC_tallyList() {
        return C_tallyList;
    }

    public void setC_tallyList(List<Tally> c_tallyList) {
        C_tallyList = c_tallyList;
    }


}
