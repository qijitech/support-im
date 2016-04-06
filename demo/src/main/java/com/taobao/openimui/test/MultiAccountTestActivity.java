package com.taobao.openimui.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWConstants;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.contact.YWContactFactory;
import com.alibaba.mobileim.conversation.EServiceContact;
import com.alibaba.mobileim.fundamental.widget.YWAlertDialog;
import com.alibaba.mobileim.login.YWLoginState;
import com.alibaba.mobileim.ui.thridapp.ParamConstant;
import com.alibaba.mobileim.utility.YWTrackUtil;
import com.alibaba.openIMUIDemo.LoginActivity;
import com.alibaba.openIMUIDemo.R;
import com.taobao.openimui.demo.DemoApplication;
import com.taobao.openimui.sample.LoginSampleHelper;

import java.util.List;

/**
 * Created by mayongge on 15-9-10.
 */
public class MultiAccountTestActivity extends Activity{

    private final static String TAG = "MultiAccountTestActivity";
    private YWIMKit mIMKit;
    private Spinner mSpinner;
    private ArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIMKit = LoginSampleHelper.getInstance().getIMKit();
        if (mIMKit == null) {
            return;
        }
        setContentView(R.layout.demo_multi_account_test);
        TextView openChattingActivity = (TextView) findViewById(R.id.open_chatting_activity);
        openChattingActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = mIMKit.getChattingActivityIntent("朗风");
                intent.putExtra(ParamConstant.ITEMID, "45107173274");
                startActivity(intent);
            }
        });

        TextView logout = (TextView)findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(R.id.logout);
            }
        });
        initMultiAccount();
        initTest();
        YWLog.e(TAG, "onCreate");
    }

    protected void initMultiAccount() {
        TextView addNewAccount = (TextView) findViewById(R.id.add_new_account);
        addNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginSampleHelper.getInstance().setAutoLoginState(YWLoginState.idle);
                Intent intent1 = new Intent(MultiAccountTestActivity.this, LoginActivity.class);
                startActivity(intent1);
            }
        });


        mSpinner = (Spinner) findViewById(R.id.spinner);
        List<String> accountList = mIMKit.getLoginAccountList();
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, accountList);
        //设置下拉列表风格
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将适配器添加到spinner中去
        mSpinner.setAdapter(mAdapter);
        mSpinner.setVisibility(View.VISIBLE);//设置默认显示
        mSpinner.setOnItemSelectedListener(listener);
    }

    AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String userId = parent.getItemAtPosition(position).toString();
            mIMKit.switchAccount(userId);
            mIMKit = YWAPI.getIMKitInstance(userId);
            LoginSampleHelper.getInstance().setIMKit(mIMKit);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case R.id.logout: {
                AlertDialog.Builder builder = new YWAlertDialog.Builder(this);
                builder.setMessage(getResources().getString(R.string.quit_confirm))
                        .setCancelable(false)
                        .setPositiveButton(R.string.confirm,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        logout();
                                    }
                                })
                        .setNegativeButton(R.string.cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.dismiss();
                                    }
                                });
                AlertDialog dialog = builder.create();
                return dialog;

            }
        }
        return super.onCreateDialog(id);

    }


    public void logout() {
        // openIM SDK提供的登录服务
        IYWLoginService mLoginService = mIMKit.getLoginService();
        mLoginService.logout(new IWxCallback() {
            //此时logout已关闭所有基于IMBaseActivity的OpenIM相关Actiivity，s
            @Override
            public void onSuccess(Object... arg0) {
                Toast.makeText(DemoApplication.getContext(), "退出成功", Toast.LENGTH_SHORT).show();
                String account = YWAPI.getCurrentUser();
                mIMKit = YWAPI.getIMKitInstance(account);
                LoginSampleHelper.getInstance().setIMKit(mIMKit);
//                setSpinnerItemSelected();
                //如果所有账号都退出了则跳转到登录页面
                if (YWAPI.getLoginAccountList() == null || YWAPI.getLoginAccountList().size() == 0) {
                    finish();
                    LoginSampleHelper.getInstance().setAutoLoginState(YWLoginState.idle);
                    Intent intent = new Intent(DemoApplication.getContext(), LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                setSpinnerItemSelected();
            }

            @Override
            public void onProgress(int arg0) {

            }

            @Override
            public void onError(int arg0, String arg1) {
                Toast.makeText(DemoApplication.getContext(), "退出失败,请重新登录", Toast.LENGTH_SHORT).show();
                LoginSampleHelper.getInstance().loginOut_Sample();
            }
        });

        //本来应该放在上面的onSuccess中，但是退出成功之后，所有基于IMBaseActivity的类会被Finish掉，从而导致MainTabActivity被Finish掉，
        //这样就会出现程序先退出、再启动，有明显的间隔，因此把intent放在下面，减少间隔。
//        Intent intent = new Intent(DemoApplication.getContext(), LoginActivity.class);
//        startActivity(intent);

    }

    private void setSpinnerItemSelected() {
        String account = YWAPI.getCurrentUser();
        int count = mAdapter.getCount();
        mSpinner.setAdapter(mAdapter);
        for (int i = 0; i < count; ++i) {
            if (account.equals(mAdapter.getItem(i).toString())) {
                YWLog.e("MultiAccountTestActivity", "itemCount = " + mSpinner.getCount());
                mSpinner.setSelection(i);

                mIMKit.switchAccount(account);
                mIMKit = YWAPI.getIMKitInstance(account);
                LoginSampleHelper.getInstance().setIMKit(mIMKit);
                break;
            }
        }
    }

    private void initTest(){

        TextView testInit = (TextView) findViewById(R.id.init_track);
        testInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YWTrackUtil.init(LoginSampleHelper.getInstance().getIMKit().getIMCore().getLoginUserId(), LoginSampleHelper.APP_KEY, null);
            }
        });

        TextView update = (TextView) findViewById(R.id.update_track_info);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YWTrackUtil.updateExtraInfo("", "", null);
            }
        });

        TextView test1 = (TextView) findViewById(R.id.open_test_activity1);
        test1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MultiAccountTestActivity.this, TestActivity1.class);
                startActivity(intent);
            }
        });

        TextView test2 = (TextView) findViewById(R.id.open_test_activity2);
        test2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MultiAccountTestActivity.this, TestActivity2.class);
                startActivity(intent);
            }
        });

        TextView test3 = (TextView) findViewById(R.id.open_conversation_activity);
        test3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EServiceContact contact = new EServiceContact("openim官方客服:android", 0);
                contact.changeToMainAccount = false;
                Intent intent = LoginSampleHelper.getInstance().getIMKit().getChattingActivityIntent(contact);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setSpinnerItemSelected();
        YWLog.e(TAG, "onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        YWLog.e(TAG, "onDestroy");
    }
}
