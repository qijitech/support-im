package com.taobao.openimui.sample;

import android.app.Activity;
import android.content.Intent;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.conversation.EServiceContact;

public class OpenEServiceChattingSampleHelper {
	/**
	 * E客服分流
	 * 
	 * @param context
	 */
	public static void openEserviceChatting_Sample(Activity context){
		//参数为客服ID和groupId
		EServiceContact contact = new EServiceContact("userid", 0);
		
		YWIMKit imKit = LoginSampleHelper.getInstance().getIMKit();
		Intent intent = imKit.getChattingActivityIntent(contact);
		context.startActivity(intent);
	}
}
