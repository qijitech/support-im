package com.taobao.openimui.contact;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.conversation.YWMessage;
import com.alibaba.mobileim.kit.common.YWAsyncBaseAdapter;
import com.alibaba.mobileim.kit.contact.ContactHeadLoadHelper;
import com.alibaba.mobileim.lib.model.message.YWSystemMessage;
import com.alibaba.openIMUIDemo.R;
import com.taobao.openimui.sample.LoginSampleHelper;

import java.util.List;

public class ContactSystemMessageAdapter extends YWAsyncBaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<YWMessage> mMessageList;
    private ContactHeadLoadHelper mContactHeadLoadHelper;
    private String mAppKey;

    public ContactSystemMessageAdapter(Context context, List<YWMessage> messages) {
        mContext = context;
        mMessageList = messages;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContactHeadLoadHelper = new ContactHeadLoadHelper((Activity)context, null);
        mAppKey = LoginSampleHelper.getInstance().getIMKit().getIMCore().getAppKey();
    }

    private class ViewHolder {
        TextView showName;
        TextView message;
        TextView agreeButton;
        TextView result;
        ImageView head;
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

    private IYWContactService getContactService(){
        return LoginSampleHelper.getInstance().getIMKit().getContactService();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.demo_contact_system_message_item, parent, false);
            holder = new ViewHolder();
            holder.showName = (TextView) convertView.findViewById(R.id.contact_title);
            holder.head = (ImageView) convertView.findViewById(R.id.head);
            holder.message = (TextView) convertView.findViewById(R.id.invite_message);
            holder.agreeButton = (TextView) convertView
                    .findViewById(R.id.agree);
            holder.result = (TextView) convertView.findViewById(R.id.result);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mMessageList != null) {
            final YWMessage msg = mMessageList.get(position);
            final YWSystemMessage message = (YWSystemMessage) msg;
            String authorUserId = message.getAuthorUserId();
            String showName=authorUserId;
            holder.showName.setText(showName+" 申请加你为好友");
            holder.message.setText("备注: "+message.getMessageBody().getContent());
            holder.agreeButton.setText("接受");
            holder.agreeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ContactSystemMessageActivity) mContext).acceptToBecomeFriend(msg);
                }
            });

            if (message.isAccepted()){
                holder.agreeButton.setVisibility(View.GONE);
                holder.result.setVisibility(View.VISIBLE);
                holder.result.setText("已添加");
            } else if (message.isIgnored()){
                holder.agreeButton.setVisibility(View.GONE);
                holder.result.setVisibility(View.VISIBLE);
                holder.result.setText("已忽略");
            } else {
                holder.agreeButton.setVisibility(View.VISIBLE);
                holder.result.setVisibility(View.GONE);
            }

            mContactHeadLoadHelper.setHeadView(holder.head,authorUserId, mAppKey,true);
        }
        return convertView;
    }

    @Override
    public void loadAsyncTask() {

    }

}
