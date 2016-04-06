package com.taobao.openimui.sample;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.alibaba.mobileim.YWChannel;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.kit.common.IMBaseActivity;
import com.alibaba.mobileimexternal.ui.FriendsFragment;
import com.alibaba.openIMUIDemo.R;

/**
 * 群组折叠型联系人界面
 *
 * @modify shuheng
 *
 */

public class ExpandableContactsActivitySample extends IMBaseActivity {
	private static final String TAG = ExpandableContactsActivitySample.class
			.getSimpleName();
	private FriendsFragment mCurrentFrontFragment;
	private View mWxcontactsContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setTheme(YWChannel.getIdByName("style",
				"Aliwx_ConverationStyle_default"));
		super.onCreate(savedInstanceState);
		YWLog.d(TAG, "onCreate");
		//TOASK 
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
					WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		}
		setContentView(R.layout.aliwx_contacts_layout);
		mWxcontactsContainer = findViewById(R.id.wx_contacts_container);

		if (savedInstanceState != null) {
			boolean isKilled = savedInstanceState.getBoolean(
					ONSAVEINSTANCESTATE, false);
			if (isKilled) {
				this.finish();
				return;
			}
		}
		createFragment();

	}

	private void createFragment() {

		mWxcontactsContainer.setVisibility(View.VISIBLE);
		mCurrentFrontFragment = new FriendsFragment();
		getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.wx_contacts_container, mCurrentFrontFragment)
				.commit();
		YWLog.d(TAG, "createFragment");
	}



	
}