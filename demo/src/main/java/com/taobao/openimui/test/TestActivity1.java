package com.taobao.openimui.test;

import android.os.Bundle;

import com.alibaba.mobileim.kit.track.TrackBaseActivity;
import com.alibaba.openIMUIDemo.R;

public class TestActivity1 extends TrackBaseActivity {

    private static final String TAG = "TestActivity1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_1);
        //设置当前页面的唯一标识，建议使用当前页面的类名
        setYWTrackTitleAndUrl(TAG, "https://www.taobao.com/");
    }
}
