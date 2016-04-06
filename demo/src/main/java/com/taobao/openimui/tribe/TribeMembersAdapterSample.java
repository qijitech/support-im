package com.taobao.openimui.tribe;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.contact.YWOnlineContact;
import com.alibaba.mobileim.gingko.model.tribe.YWTribeMember;
import com.alibaba.mobileim.kit.common.IMUtility;
import com.alibaba.mobileim.kit.common.YWAsyncBaseAdapter;
import com.alibaba.mobileim.kit.contact.YWContactHeadLoadHelper;
import com.alibaba.openIMUIDemo.R;

import java.util.List;

public class TribeMembersAdapterSample extends YWAsyncBaseAdapter {
    private static final String TAG = "TribeMembersAdapterSample";
    private Context context;
    private int max_visible_item_count;
    private LayoutInflater inflater;
    private YWContactHeadLoadHelper mContactHeadLoadHelper;
    private IYWContactService contactService;
    private List<YWTribeMember> mList;
    private int convertViewWidth;

    public TribeMembersAdapterSample(Activity context, List<YWTribeMember> list) {
        this.context = context;
        this.mList = list;
        mContactHeadLoadHelper = new YWContactHeadLoadHelper(context, this);
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private class ViewHolder {
        ImageView headView;
        TextView nick;
        TextView role;
    }

    public void refreshData(List<YWTribeMember> list) {
        mList = list;
        notifyDataSetChangedWithAsyncLoad();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= 0 && position < mList.size()) {
            return mList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void loadAsyncTask() {
        mContactHeadLoadHelper.setMaxVisible(max_visible_item_count);
        mContactHeadLoadHelper.loadAyncHead();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout.LayoutParams roleParams;
        RelativeLayout.LayoutParams headParams;
        RelativeLayout.LayoutParams nickParams;
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.demo_tribe_members_item, null);
            holder = new ViewHolder();
            holder.headView = (ImageView) convertView.findViewById(R.id.head);
            holder.nick = (TextView) convertView
                    .findViewById(R.id.nick);
            holder.role = (TextView) convertView.findViewById(R.id.role);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mList != null) {
            final YWTribeMember user = mList.get(position);
            if (user != null) {
                String name = user.getShowName();

                holder.headView.setTag(R.id.head, position);
                mContactHeadLoadHelper.setHeadView(holder.headView, user.getUserId(), user.getAppKey(), true);
                holder.nick.setText(IMUtility.getTribeShowName(user.getUserId(), user.getAppKey(), user.getTribeNick()));
                holder.nick.setVisibility(View.VISIBLE);
                holder.role.setVisibility(View.VISIBLE);
                int role = user.getTribeRole();
                if (role == YWTribeMember.ROLE_HOST) {
                    holder.role.setText("群主");
                    holder.role.setBackgroundColor(context.getResources().getColor(R.color.tribe_host));
                } else if (role == YWTribeMember.ROLE_MANAGER) {
                    holder.role.setText("管理员");
                    holder.role.setBackgroundColor(context.getResources().getColor(R.color.tribe_manager));
                } else {
                    holder.role.setVisibility(View.GONE);
                }
            }

        }

        holder.headView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        holder.role.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        holder.nick.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        try {
            convertView.measure(View.MeasureSpec.AT_MOST, View.MeasureSpec.UNSPECIFIED);
        } catch (NullPointerException e) {
            //SDK <= 17时,RelativeLayout measure会发生空指针
            if(convertViewWidth == 0) {
                WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                Point point = new Point();
                manager.getDefaultDisplay().getSize(point);
                convertViewWidth = point.x;
            }
        }
        headParams = (RelativeLayout.LayoutParams) holder.headView.getLayoutParams();
        roleParams = (RelativeLayout.LayoutParams) holder.role.getLayoutParams();
        nickParams = (RelativeLayout.LayoutParams) holder.nick.getLayoutParams();
        if(convertViewWidth == 0) {
            convertViewWidth = convertView.getMeasuredWidth() == 0 ? convertView.getWidth() : convertView.getMeasuredWidth();
        }
        if(convertViewWidth
                - (holder.headView.getMeasuredWidth() + headParams.leftMargin + headParams.rightMargin)
                - (holder.role.getMeasuredWidth() + roleParams.leftMargin + roleParams.rightMargin)
                - (nickParams.rightMargin + nickParams.leftMargin) > 0) {
            holder.nick.setMaxWidth(convertViewWidth
                    - (holder.headView.getMeasuredWidth() + headParams.leftMargin + headParams.rightMargin)
                    - (holder.role.getMeasuredWidth() + roleParams.leftMargin + roleParams.rightMargin)
                    - (nickParams.rightMargin + nickParams.leftMargin)
            );
        }
        return convertView;
    }

    public void setMax_visible_item_count(int max_visible_item_count) {
        this.max_visible_item_count = max_visible_item_count;
    }


    public static class ContactImpl implements IYWContact {
        private String userid = "", appKey = "", avatarPath = "", showName = "";
        private int status = YWOnlineContact.ONLINESTATUS_ONLINE;

        public ContactImpl(String showName, String userid, String avatarPath, String signatures, String appKey) {
            this.showName = showName;
            this.userid = userid;
            this.avatarPath = avatarPath;
            this.appKey = appKey;
        }

        public void setOnlineStatus(int status) {
            this.status = status;
        }

        @Override
        public String getUserId() {
            return userid;
        }

        @Override
        public String getAppKey() {
            return appKey;
        }

        @Override
        public String getAvatarPath() {
            return avatarPath;
        }

        @Override
        public String getShowName() {
            return showName;
        }

        public int getOnlineStatus() {
            return status;
        }

    }

}
