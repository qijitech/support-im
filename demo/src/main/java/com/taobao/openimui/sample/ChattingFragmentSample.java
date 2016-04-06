package com.taobao.openimui.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.kit.chat.ChattingFragment;
import com.alibaba.openIMUIDemo.R;

/**
 * Created by mayongge on 15-9-18.
 */
public class ChattingFragmentSample extends FragmentActivity{

    private static final String TAG = "ChattingActivity";
    public static final String TARGET_ID = "targetId";

    private Fragment mCurrentFrontFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_chatting_activity);
        createFragment();
        YWLog.i(TAG, "onCreate");
    }

    private void createFragment(){
        Intent intent = getIntent();
        String targetId = intent.getStringExtra(TARGET_ID);
        mCurrentFrontFragment = LoginSampleHelper.getInstance().getIMKit().getChattingFragment(targetId);
        getSupportFragmentManager().beginTransaction().replace(R.id.wx_chat_container, mCurrentFrontFragment).commit();
    }


    /**
     * 必须实现该方法，且该方法的实现必须跟以下示例代码完全一致！
     * todo 因为拍照和选择照片的时候会回调该方法，如果没有按以下方式覆写该方法会导致拍照和选择照片时应用crash或拍照和选择照片无效!
     * @param arg0
     * @param arg1
     * @param arg2
     */
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (mCurrentFrontFragment != null) {
            mCurrentFrontFragment.onActivityResult(arg0, arg1, arg2);
        }
    }
    /**
     * 必须实现该方法，且该方法的实现必须跟以下示例代码完全一致！
     */
    @Override
    public void onBackPressed() {

        if (mCurrentFrontFragment != null && mCurrentFrontFragment.isVisible()) {

            if (mCurrentFrontFragment instanceof ChattingFragment &&((ChattingFragment)mCurrentFrontFragment).onBackPressed()) {
                return;
            }
        }

        super.onBackPressed();
    }
}
