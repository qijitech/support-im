package com.taobao.openimui.imcore;

import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMCore;
import com.taobao.openimui.sample.InitHelper;

/**
 * Created by mayongge on 15/12/17.
 */
public class InitSample {
    public static String USER_ID = ""; //登录id
    public static String APP_KEY = ""; //登录用户appKey
    private static InitSample  sInstance = new InitSample();

    private YWIMCore mIMCore;

    public static InitSample getInstance(){
        return sInstance;
    }

    public YWIMCore getIMCore(){
        return mIMCore;
    }

    public void initIMSDK(){
        mIMCore = YWAPI.createIMCore(USER_ID, APP_KEY);
    }
}
