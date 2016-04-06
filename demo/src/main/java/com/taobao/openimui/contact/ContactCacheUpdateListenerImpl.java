package com.taobao.openimui.contact;

import com.alibaba.mobileim.contact.IYWContactCacheUpdateListener;
import com.taobao.openimui.common.Notification;
import com.taobao.openimui.demo.DemoApplication;

/**
 * Created by ShuHeng on 16/2/26.
 */
public class ContactCacheUpdateListenerImpl implements IYWContactCacheUpdateListener {

    /**
     * 好友缓存发生变化(联系人备注修改、联系人新增和减少等)，可以刷新使用联系人缓存的UI
     * todo 该回调在UI线程回调 ，请勿做太重的操作
     *
     * @param currentUserid                 当前登录账户
     * @param currentAppkey                 当前Appkey
     */
    @Override
    public void onFriendCacheUpdate(String currentUserid, String currentAppkey) {
        Notification.showToastMsg(DemoApplication.getContext(), "好友缓存发生变化");

    }
}
