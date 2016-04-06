package com.taobao.openimui.sample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.mobileim.channel.constant.YWProfileSettingsConstants;
import com.alibaba.openIMUIDemo.R;

public class TribeMsgRecTypeSetActivity extends Activity implements View.OnClickListener{

    private int flag;
    private ImageView mTribeMsgRecCheck;
    private ImageView mTribeMsgRecNotRemindCheck;
    private ImageView mTribeMsgRejCheck;

    private RelativeLayout mTribeMsgRecLayout;
    private RelativeLayout mTribeMsgRecNotRemindLayout;
    private RelativeLayout mTribeMsgRejLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tribe_msg_rec_type_set);
        initTitle();
        Intent intent = getIntent();
        flag = intent.getIntExtra("Flag", YWProfileSettingsConstants.TRIBE_MSG_REJ);
        initViews();
        initMsgRecFlag(flag);
    }

    private void initTitle() {
        View titleView = findViewById(R.id.title_bar);
        titleView.setVisibility(View.VISIBLE);
        titleView.setBackgroundColor(Color.parseColor("#00b4ff"));

        TextView leftButton = (TextView) findViewById(R.id.left_button);
        leftButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.demo_common_back_btn_white, 0, 0, 0);
        leftButton.setText("返回");
        leftButton.setTextColor(Color.WHITE);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(flag);
                finish();
            }
        });

        TextView title = (TextView) findViewById(R.id.title_self_title);
        title.setText("聊天设置");
        title.setTextColor(Color.WHITE);
    }

    private void initViews() {
        mTribeMsgRecLayout = (RelativeLayout) findViewById(R.id.receive_and_remind_layout);
        mTribeMsgRecNotRemindLayout = (RelativeLayout) findViewById(R.id.only_receive_layout);
        mTribeMsgRejLayout = (RelativeLayout) findViewById(R.id.not_receive_layout);

        mTribeMsgRecCheck = (ImageView) findViewById(R.id.receive_and_remind);
        mTribeMsgRecNotRemindCheck = (ImageView) findViewById(R.id.only_receive);
        mTribeMsgRejCheck = (ImageView) findViewById(R.id.not_receive);

        mTribeMsgRecLayout.setOnClickListener(this);
        mTribeMsgRecNotRemindLayout.setOnClickListener(this);
        mTribeMsgRejLayout.setOnClickListener(this);
    }

    public static Intent getTribeMsgRecTypeSetActivityIntent(Context context, int flag) {
        Intent intent = new Intent(context, TribeMsgRecTypeSetActivity.class);
        intent.putExtra("Flag", flag);
        return intent;
    }

    private void initMsgRecFlag(int flag) {
        switch (flag) {
            case YWProfileSettingsConstants.TRIBE_MSG_REC:
                mTribeMsgRecCheck.setVisibility(View.VISIBLE);
                mTribeMsgRecNotRemindCheck.setVisibility(View.GONE);
                mTribeMsgRejCheck.setVisibility(View.GONE);
                break;
            case YWProfileSettingsConstants.TRIBE_MSG_REC_NOT_REMIND:
                mTribeMsgRecCheck.setVisibility(View.GONE);
                mTribeMsgRecNotRemindCheck.setVisibility(View.VISIBLE);
                mTribeMsgRejCheck.setVisibility(View.GONE);
                break;
            case YWProfileSettingsConstants.TRIBE_MSG_REJ:
                mTribeMsgRecCheck.setVisibility(View.GONE);
                mTribeMsgRecNotRemindCheck.setVisibility(View.GONE);
                mTribeMsgRejCheck.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.receive_and_remind_layout:
                flag = YWProfileSettingsConstants.TRIBE_MSG_REC;
                Intent intent1 = new Intent();
                intent1.putExtra("Flag", flag);
                setResult(RESULT_OK, intent1);
                initMsgRecFlag(flag);
                finish();
                break;
            case R.id.only_receive_layout:
                flag = YWProfileSettingsConstants.TRIBE_MSG_REC_NOT_REMIND;
                Intent intent2 = new Intent();
                intent2.putExtra("Flag", flag);
                setResult(RESULT_OK, intent2);
                initMsgRecFlag(flag);
                finish();
                break;
            case R.id.not_receive_layout:
                flag = YWProfileSettingsConstants.TRIBE_MSG_REJ;
                Intent intent3 = new Intent();
                intent3.putExtra("Flag", flag);
                setResult(RESULT_OK, intent3);
                initMsgRecFlag(flag);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setResult(flag);
        super.onBackPressed();
    }
}
