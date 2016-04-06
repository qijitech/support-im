package com.taobao.openimui.sample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMContactsUI;
import com.alibaba.openIMUIDemo.R;
import com.taobao.openimui.contact.FindContactActivity;

/**
 * 联系人界面UI的定制点(根据需要实现相应的接口来达到联系人界面)，不设置则使用云旺默认的实现
 * 调用方设置的回调，必须继承BaseAdvice 根据不同的需求实现 不同的 开放的 Advice
 * com.alibaba.mobileim.aop.pointcuts包下开放了不同的Advice.通过实现多个接口，组合成对不同的ui界面的定制
需要在application中将这个Advice绑定。设置以下代码
 * AdviceBinder.bindAdvice(PointCutEnum.CONTACTS_UI_POINTCUT, ContactsUICustomSample.class);
 *
 * @author shuheng
 */
public class ContactsUICustomSample extends IMContactsUI {

    public ContactsUICustomSample(Pointcut pointcut) {
        super(pointcut);
    }



    /**
     * 返回联系人自定义标题
     *
     * @param fragment
     * @param context
     * @param inflater
     * @return
     */
    @Override
    public View getCustomTitle(final Fragment fragment, final Context context, LayoutInflater inflater) {
        //TODO 重要：必须以该形式初始化customView---［inflate(R.layout.**, new RelativeLayout(context),false)］------，以让inflater知道父布局的类型，否则布局xml**中定义的高度和宽度无效，均被默认的wrap_content替代
        RelativeLayout customView = (RelativeLayout) inflater
                .inflate(R.layout.demo_custom_contacts_title_bar, new RelativeLayout(context), false);
        customView.setBackgroundColor(Color.parseColor("#00b4ff"));
        TextView title = (TextView) customView.findViewById(R.id.title_txt);
        title.setText("联系人");
        title.setTextSize(15);
        title.setTextColor(Color.WHITE);
        title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Toast.makeText(context, "click ", Toast.LENGTH_SHORT).show();

            }
        });
        TextView backButton = (TextView) customView.findViewById(R.id.left_button);
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                fragment.getActivity().finish();
            }
        });
        backButton.setVisibility(View.GONE);
        ImageView rightButton = (ImageView) customView.findViewById(R.id.title_button);
        rightButton.setVisibility(View.VISIBLE);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent().setClass(context, FindContactActivity.class);
                context.startActivity(intent);
            }
        });
        return customView;
    }

}
