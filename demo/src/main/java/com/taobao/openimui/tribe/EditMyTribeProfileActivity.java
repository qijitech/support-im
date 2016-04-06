package com.taobao.openimui.tribe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.mobileim.WXAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeType;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.alibaba.mobileim.utility.IMNotificationUtils;
import com.alibaba.openIMUIDemo.R;
import com.taobao.openimui.sample.LoginSampleHelper;

/**
 * Created by weiquanyun on 15/11/2.
 */
public class EditMyTribeProfileActivity extends Activity {

    private long tribeId;
    private String userId;
    private String oldNick;
    private YWIMKit mIMKit;
    private IYWTribeService mTribeService;
    private EditText mNickInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_edit_my_tribe_profile);
        Intent intent = getIntent();
        if(intent != null) {
            tribeId = intent.getLongExtra(TribeConstants.TRIBE_ID, 0);
            oldNick = intent.getStringExtra(TribeConstants.TRIBE_NICK);
        }
        if (tribeId == 0) {
           //TODO 群ID没获取到的处理逻辑
        }
        init();
        initTitle();
        initViews();
    }

    private void init() {
        mIMKit = LoginSampleHelper.getInstance().getIMKit();
        mTribeService = mIMKit.getTribeService();
        userId = WXAPI.getInstance().getLongLoginUserId();
    }

    private void initViews() {
        mNickInput = (EditText) findViewById(R.id.my_profile_input);
        mNickInput.setText(oldNick);
    }

    private void initTitle() {
        RelativeLayout titleBar = (RelativeLayout) findViewById(R.id.title_bar);
        titleBar.setBackgroundColor(Color.parseColor("#00b4ff"));
        titleBar.setVisibility(View.VISIBLE);

        TextView titleView = (TextView) findViewById(R.id.title_self_title);
        titleView.setText("编辑群名片");
        TextView leftButton = (TextView) findViewById(R.id.left_button);
        leftButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.demo_common_back_btn_white, 0, 0, 0);
        leftButton.setTextColor(Color.WHITE);
        leftButton.setText("返回");
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleView.setTextColor(Color.WHITE);

        TextView rightButton = (TextView) findViewById(R.id.right_button);
        rightButton.setText("提交");
        rightButton.setTextColor(Color.WHITE);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadModifiedUserNick(mNickInput.getText().toString());
            }
        });
    }

    private void uploadModifiedUserNick(final String userNick){
        mTribeService.modifyTribeUserNick(tribeId, mIMKit.getIMCore().getAppKey(), userId, userNick, new IWxCallback() {
            @Override
            public void onSuccess(Object... result) {
                IMNotificationUtils.showToast("修改成功", EditMyTribeProfileActivity.this);
                Intent intent = new Intent();
                intent.putExtra(TribeConstants.TRIBE_NICK, userNick);
                setResult(Activity.RESULT_OK, intent);
                EditMyTribeProfileActivity.this.finish();
            }

            @Override
            public void onError(int code, String info) {
                IMNotificationUtils.showToast("修改失败",EditMyTribeProfileActivity.this);
            }

            @Override
            public void onProgress(int progress) {

            }
        });
    }
}
