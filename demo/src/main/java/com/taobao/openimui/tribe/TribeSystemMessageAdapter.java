package com.taobao.openimui.tribe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.gingko.model.tribe.YWTribe;
import com.alibaba.mobileim.kit.common.YWAsyncBaseAdapter;
import com.alibaba.mobileim.lib.model.message.YWSystemMessage;
import com.alibaba.openIMUIDemo.R;
import com.taobao.openimui.sample.LoginSampleHelper;

import java.util.List;

/**
 * Created by mayongge on 15-10-20.
 */
public class TribeSystemMessageAdapter extends YWAsyncBaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<YWMessage> mMessageList;

    public TribeSystemMessageAdapter(Context context, List<YWMessage> messages) {
        mContext = context;
        mMessageList = messages;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ViewHolder {
        TextView tribeName;
        TextView message;
        TextView agreeButton;
        TextView ignoreButton;
        TextView result;
    }

    public void refreshData(List<YWMessage> list){
        mMessageList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMessageList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMessageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.demo_system_message_item, parent, false);
            holder = new ViewHolder();
            holder.tribeName = (TextView) convertView.findViewById(R.id.tribe_name);
            holder.message = (TextView) convertView.findViewById(R.id.invite_message);
            holder.agreeButton = (TextView) convertView
                    .findViewById(R.id.agree);
            holder.ignoreButton = (TextView) convertView.findViewById(R.id.ignore);
            holder.result = (TextView) convertView.findViewById(R.id.result);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mMessageList != null) {
            final YWMessage msg = mMessageList.get(position);
            final YWSystemMessage message = (YWSystemMessage) msg;
            long tid = Long.valueOf(message.getAuthorId());
            final YWTribe tribe = LoginSampleHelper.getInstance().getIMKit().getTribeService().getTribe(tid);
            holder.tribeName.setText(tribe.getTribeName());
            holder.message.setText(message.getMessageBody().getContent());
            holder.agreeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String recommender = message.getRecommender();
                    ((TribeSystemMessageActivity)mContext).acceptToJoinTribe(msg);
                }
            });
            holder.ignoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((TribeSystemMessageActivity)mContext).refuseToJoinTribe(msg);
                }
            });

            if (message.isAccepted()){
                holder.agreeButton.setVisibility(View.GONE);
                holder.ignoreButton.setVisibility(View.GONE);
                holder.result.setVisibility(View.VISIBLE);
                holder.result.setText("已同意");
            } else if (message.isIgnored()){
                holder.agreeButton.setVisibility(View.GONE);
                holder.ignoreButton.setVisibility(View.GONE);
                holder.result.setVisibility(View.VISIBLE);
                holder.result.setText("已忽略");
            } else {
                holder.agreeButton.setVisibility(View.VISIBLE);
                holder.ignoreButton.setVisibility(View.VISIBLE);
                holder.result.setVisibility(View.GONE);
            }
        }
        return convertView;
    }

    @Override
    public void loadAsyncTask() {

    }

}
