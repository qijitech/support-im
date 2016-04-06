package com.taobao.openimui.common;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.text.TextUtils;

import com.alibaba.mobileim.YWChannel;
import com.alibaba.mobileim.channel.IMChannel;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.openIMUIDemo.R;
import com.alibaba.wxlib.util.SysUtil;

/**
 * Created by zhaoxu on 2015/8/26.
 * 调试信息的一些输出，仅方便内部调试用，开发者不需要关心
 */
public class DebugInfo {
    public static void init(){
        IMChannel.setDebugDataHandler(mDebugDataHandler);
    }

    private static final String TAG = "DebugInfo";
    private static IMChannel.DebugDataHandler mDebugDataHandler = new IMChannel.DebugDataHandler() {
        @Override
        public void onHandleDebugData(String key, String value) {
            YWLog.d(TAG, "onHandleDebugData key=" + key + " value=" + value);
            if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)){
                return;
            }

            if ("connstatus".equals(key)){
                //登录状态的改变
                if ("1".equals(value)){
                    showNotification(1,"连接成功");
                }else if ("0".equals(value)){
                    showNotification(1,"连接断开");
                }
            }else if ("activeim".equals(key)){
                showNotification(2,"收到activeim");
            }
        }
    };

    @TargetApi(17)
    public static void showNotification(int id, String content){
        Notification.Builder builder = new Notification.Builder(SysUtil.sApp)
                .setSmallIcon(R.drawable.aliwx_s001)
                .setTicker("zhaoxuTest")
                .setWhen(System.currentTimeMillis())
                .setOnlyAlertOnce(true)
                .setAutoCancel(true)
                .setContentTitle("连接状态")
                .setContentText(content);

        NotificationManager nm = (NotificationManager) YWChannel.getApplication()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(id, builder.build());
    }
}
