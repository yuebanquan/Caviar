package com.example.terminal_work.litepal;

/**
 * 已废弃
 */
public class SqlAll {
    public AssetAccount assetAccount;
    public Classify classify;
    public Tally tally;
    public User user;
    public Wish wish;

    public SqlAll() {
        assetAccount = new AssetAccount();
        classify = new Classify();
        tally = new Tally();
        user = new User();
        wish = new Wish();
    }
}
