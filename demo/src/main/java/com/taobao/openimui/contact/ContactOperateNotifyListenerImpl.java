package com.taobao.openimui.contact;

import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactOperateNotifyListener;
import com.taobao.openimui.common.Notification;
import com.taobao.openimui.demo.DemoApplication;

/**
 * Created by ShuHeng on 16/2/26.
 */
public class ContactOperateNotifyListenerImpl implements IYWContactOperateNotifyListener {
    /**
     * 用户请求加你为好友
     * todo 该回调在UI线程回调 ，请勿做太重的操作
     *
     * @param contact 用户的信息
     * @param message 附带的备注
     */
    @Override
    public void onVerifyAddRequest(IYWContact contact, String message) {
        Notification.showToastMsg(DemoApplication.getContext(), contact.getUserId()+"用户请求加你为好友");

//                 //增加未读数的显示
//                 YWConversation conversation = mIMKit.getConversationService().getCustomConversationByConversationId(SYSTEM_FRIEND_REQ_CONVERSATION);
//                 if ( conversation!= null) {
//                     YWCustomConversationUpdateModel model = new YWCustomConversationUpdateModel();
//                     model.setIdentity(SYSTEM_FRIEND_REQ_CONVERSATION);
//                     model.setLastestTime(new Date().getTime());
//                     model.setUnreadCount(conversation.getUnreadCount() + 1);
//                     if(conversation.getConversationBody() instanceof YWCustomConversationBody){
//                         model.setExtraData(((YWCustomConversationBody)conversation.getConversationBody()).getExtraData());
//                     }
//                     if(mConversationService!=null)
//                     mConversationService.updateOrCreateCustomConversation(model);
//                 }

    }

    /**
     * 用户接受了你的好友请求
     * todo 该回调在UI线程回调 ，请勿做太重的操作
     *
     * @param contact 用户的信息
     */
    @Override
    public void onAcceptVerifyRequest(IYWContact contact) {
        Notification.showToastMsg(DemoApplication.getContext(),contact.getUserId()+"用户接受了你的好友请求");
    }
    /**
     * 用户拒绝了你的好友请求
     * todo 该回调在UI线程回调 ，请勿做太重的操作
     * @param  contact 用户的信息
     */
    @Override
    public void onDenyVerifyRequest(IYWContact contact) {
        Notification.showToastMsg(DemoApplication.getContext(),contact.getUserId()+"用户拒绝了你的好友请求");
    }

    /**
     * 云旺服务端（或其它终端）进行了好友添加操作
     * todo 该回调在UI线程回调 ，请勿做太重的操作
     *
     * @param contact 用户的信息
     */
    @Override
    public void onSyncAddOKNotify(IYWContact contact) {
        Notification.showToastMsg(DemoApplication.getContext(),"云旺服务端（或其它终端）进行了好友添加操作对"+contact.getUserId());

    }

    /**
     * 用户从好友名单删除了您
     * todo 该回调在UI线程回调 ，请勿做太重的操作
     *
     * @param contact 用户的信息
     */
    @Override
    public void onDeleteOKNotify(IYWContact contact) {
        Notification.showToastMsg(DemoApplication.getContext(),contact.getUserId()+"用户从好友名单删除了您");
    }
}
