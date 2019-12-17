package com.example.terminal_work.litepal;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * User —— 用户表
 *
 * U_id         用户账号     唯一约束
 * U_name       用户名      非空
 * U_password   密码        非空
 */
public class User extends LitePalSupport {
    @Column()
    private int U_id;

    @Column(nullable = false)
    private String U_name;

    @Column(nullable = false)
    private String U_password;

    public int getU_id() {
        return U_id;
    }

    public void setU_id(int u_id) {
        U_id = u_id;
    }

    public String getU_name() {
        return U_name;
    }

    public void setU_name(String u_name) {
        U_name = u_name;
    }

    public String getU_password() {
        return U_password;
    }

    public void setU_password(String u_password) {
        U_password = u_password;
    }
}
