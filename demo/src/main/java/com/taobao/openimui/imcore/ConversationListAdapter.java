package com.taobao.openimui.imcore;

import android.view.View;
import android.view.ViewGroup;

import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.kit.common.YWAsyncBaseAdapter;

import java.util.List;

/**
 * Created by mayongge on 15/12/17.
 */
public class ConversationListAdapter extends YWAsyncBaseAdapter {

    private List<YWConversation> mList;

    public ConversationListAdapter(List<YWConversation> list){
        mList = list;
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public void loadAsyncTask() {

    }
}
