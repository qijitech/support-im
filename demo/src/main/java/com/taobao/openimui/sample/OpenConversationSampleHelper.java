package com.taobao.openimui.sample;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.util.YWLog;

/**
 * 打开最近会话列表
 * @author zhaoxu
 *
 */
public class OpenConversationSampleHelper {
	/**
	 * 获取打开最近会话列表界面的Intent
	 * 
	 * @param context
	 */
	public static Intent getOpenConversationListIntent_Sample(Activity context){
		YWIMKit imKit = LoginSampleHelper.getInstance().getIMKit();
		if (imKit == null) {
			Toast.makeText(context, "未初始化", Toast.LENGTH_SHORT).show();
            YWLog.e("DEMO", "getOpenConversationListIntent_Sample fail");
			return null;
		}

		//这里是使用了Intent的方式，同时SDK也是支持Fragment的方式
		//imKit.getConversationFragment();
		//注意：如果使用Fragment的方式，请在对应的Activity中增加主题的设置(修改AndroidManifest.xml文件)：android:theme="@style/Aliwx_ConverationStyle_default"
		//具体请参考：https://baichuan.taobao.com/portal/doc?articleId=538

		Intent intent = imKit.getConversationActivityIntent();
		
		return intent;
	}
}
