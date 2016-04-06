package com.taobao.openimui.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.ui.contact.ContactsFragment;
import com.alibaba.mobileim.ui.contact.adapter.ContactsAdapter;
import com.alibaba.openIMUIDemo.R;
import java.util.List;

public class SelectContactToSendCardActivity extends FragmentActivity {

    private static final String TAG = "SelectContactToSendCardActivity";

    private YWIMKit mIMKit;
    private ContactsFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_activity_select_contact);

        mIMKit = LoginSampleHelper.getInstance().getIMKit();

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
        titleView.setText("选择联系人");


        TextView rightButton = (TextView) findViewById(R.id.right_button);
        rightButton.setText("完成");
        rightButton.setTextColor(Color.WHITE);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsAdapter adapter = mFragment.getContactsAdapter();
                List<String> list = adapter.getSelectedList();
                if (list != null && list.size() > 0){
                    ChattingOperationCustomSample.selectContactListener.onSelectCompleted(list);
                    finish();
                }
            }
        });
    }

    private void createFragment(){
        mFragment =mIMKit.getContactsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ContactsFragment.SEND_CARD, ContactsFragment.SEND_CARD);
        mFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.contact_list_container, mFragment).commit();
    }
}
