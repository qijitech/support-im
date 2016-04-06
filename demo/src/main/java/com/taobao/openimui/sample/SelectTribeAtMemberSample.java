package com.taobao.openimui.sample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMSelectTribeAtMemeberPageUI;
import com.alibaba.openIMUIDemo.R;

/**
 * Created by weiquanyun on 15/12/22.
 */
public class SelectTribeAtMemberSample extends IMSelectTribeAtMemeberPageUI {

    public SelectTribeAtMemberSample(Pointcut pointcut) {
        super(pointcut);
    }

    /**
     * 获取自定义title
     * @param activity
     * @param context
     * @param inflater
     * @return
     */
    @Override
    public View getCustomTitle(final Activity activity, Context context, LayoutInflater inflater) {
        View view = LayoutInflater.from(activity).inflate(R.layout.demo_custom_select_at_member_title, new RelativeLayout(context));
        //TODO 这里设置背景最好用color的方式来设置,因为界面中的一些字体颜色需要跟titleBar主题颜色一致
        view.setBackgroundColor(activity.getResources().getColor(R.color.aliwx_color_blue));
        TextView titleText = (TextView) view.findViewById(R.id.title);
        titleText.setText("成员列表");
        titleText.setTextColor(Color.WHITE);
        TextView back = (TextView) view.findViewById(R.id.back);
        back.setVisibility(View.GONE);
        TextView titleButton = (TextView) view.findViewById(R.id.title_button);
        titleButton.setText("取消");
        titleButton.setTextColor(Color.WHITE);
        titleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        return view;
    }
}
