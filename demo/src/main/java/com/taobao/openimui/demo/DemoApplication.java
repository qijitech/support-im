package com.taobao.openimui.demo;

import android.app.Application;
import android.content.Context;
//import android.support.multidex.MultiDexApplication;

import com.alibaba.mobileim.FeedbackAPI;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.wxlib.util.SysUtil;
import com.taobao.openimui.sample.InitHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class DemoApplication extends Application {
	//云旺OpenIM的DEMO用到的Application上下文实例
	private static Context sContext;
	public static Context getContext(){
		return sContext;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();

		//todo Application.onCreate中，首先执行这部分代码，以下代码固定在此处，不要改动，这里return是为了退出Application.onCreate！！！
		if(mustRunFirstInsideApplicationOnCreate()){
			//todo 如果在":TCMSSevice"进程中，无需进行openIM和app业务的初始化，以节省内存
			return;
		}

		//初始化云旺SDK
		InitHelper.initYWSDK(this);

		//初始化反馈功能(未使用反馈功能的用户无需调用该初始化)

		InitHelper.initFeedBack(this);
	}



	private boolean mustRunFirstInsideApplicationOnCreate() {
		//必须的初始化
		SysUtil.setApplication(this);
		sContext = getApplicationContext();
		return SysUtil.isTCMSServiceProcess(sContext);
	}

}
