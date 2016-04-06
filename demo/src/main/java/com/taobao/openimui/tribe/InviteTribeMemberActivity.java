package com.taobao.openimui.tribe;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeType;
import com.alibaba.mobileim.tribe.IYWTribeService;
import com.alibaba.mobileim.ui.contact.ContactsFragment;
import com.alibaba.mobileim.ui.contact.adapter.ContactsAdapter;
import com.alibaba.openIMUIDemo.R;
import com.taobao.openimui.common.Notification;
import com.taobao.openimui.sample.LoginSampleHelper;

import java.util.List;

public class InviteTribeMemberActivity extends FragmentActivity {

    private static final String TAG = "InviteTribeMemberActivity";

    private YWIMKit mIMKit;
    private IYWTribeService mTribeService;
    private long mTribeId;

    private ContactsFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_invite_tribe_member);

        mIMKit = LoginSampleHelper.getInstance().getIMKit();
        mTribeService = mIMKit.getTribeService();
        mTribeId = getIntent().getLongExtra(TribeConstants.TRIBE_ID, 0);

        initTitle();
        createFragment();
        YWLog.i(TAG, "onCreate");
    }

    private void initTitle(){
        RelativeLayout titleBar = (RelativeLayout) findViewById(R.id.title_bar);
        titleBar.setBackgroundColor(Color.parseColor("#00b4ff"));
        titleBar.setVisibility(View.VISIBLE);

        TextView titleView = (TextView) findViewById(R.id.title_self_title);
        TextView leftButton = (TextView) findViewById(R.id.left_button);
        leftButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.demo_common_back_btn_white, 0, 0, 0);
        leftButton.setTextColor(Color.WHITE);
        leftButton.setText("取消");
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleView.setTextColor(Color.WHITE);
        titleView.setText("添加联系人");


        final YWTribeType tribeType = mTribeService.getTribe(mTribeId).getTribeType();
        TextView rightButton = (TextView) findViewById(R.id.right_button);
        rightButton.setText("完成");
        rightButton.setTextColor(Color.WHITE);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsAdapter adapter = mFragment.getContactsAdapter();
                List<String> list = adapter.getSelectedList();
                if (list != null && list.size() > 0){
                    mTribeService.inviteMembers(new IWxCallback() {
                        @Override
                        public void onSuccess(Object... result) {
                           Integer retCode = (Integer) result[0];
                            if (retCode == 0){
                                if (tribeType == YWTribeType.CHATTING_GROUP) {
                                    Notification.showToastMsg(InviteTribeMemberActivity.this, "添加群成员成功！");
                                } else {
                                    Notification.showToastMsg(InviteTribeMemberActivity.this, "群邀请发送成功！");
                                }
                                finish();
                            }
                        }

                        @Override
                        public void onError(int code, String info) {
                            Notification.showToastMsg(InviteTribeMemberActivity.this, "添加群成员失败，code = " + code + ", info = " + info);
                        }

                        @Override
                        public void onProgress(int progress) {

                        }
                    }, mTribeId, list);
                }
            }
        });
    }

    private void createFragment(){
        mFragment =mIMKit.getContactsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TribeConstants.TRIBE_OP, TribeConstants.TRIBE_INVITE);
        bundle.putLong(TribeConstants.TRIBE_ID, mTribeId);
        mFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.contact_list_container, mFragment).commit();
    }
}
