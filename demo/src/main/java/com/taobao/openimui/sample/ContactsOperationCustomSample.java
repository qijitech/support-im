package com.taobao.openimui.sample;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.alibaba.mobileim.YWConstants;
import com.alibaba.mobileim.aop.Pointcut;
import com.alibaba.mobileim.aop.custom.IMContactsOperation;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.channel.util.YWLog;
import com.alibaba.mobileim.contact.IYWContact;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.contact.YWContactManager;
import com.alibaba.mobileim.conversation.EServiceContact;
import com.alibaba.mobileim.fundamental.widget.YWAlertDialog;
import com.alibaba.mobileim.ui.contact.util.RandomNameUtil;
import com.alibaba.mobileim.utility.ToastHelper;

/**
 * 联系人界面业务的定制点(根据需要实现相应的接口来达到定制联系人界面的业务)，不设置则使用云旺默认的实现
 * 调用方设置的回调，必须继承BaseAdvice 根据不同的需求实现 不同的 开放的 Advice
 * com.alibaba.mobileim.aop.pointcuts包下开放了不同的Advice.通过实现多个接口，组合成对不同的界面的定制
 需要在application中将这个Advice绑定。设置以下代码
 * AdviceBinder.bindAdvice(PointCutEnum.CONTACTS_OP_POINTCUT, ContactsOperationCustomSample.class);
 *
 * @author shuheng
 */
public class ContactsOperationCustomSample extends IMContactsOperation {

    private String TAG=ContactsOperationCustomSample.class.getSimpleName();

    public ContactsOperationCustomSample(Pointcut pointcut) {
        super(pointcut);
    }

    /**
     * 是否同步联系人在线状态
     *
     * @param fragment 联系人页面fragment
     * @param context  联系人页面context
     * @return
     */
    @Override
    public boolean enableSyncContactOnlineStatus(Fragment fragment, Context context) {
        return true;
    }

    /**
     * 定制点击事件
     *
     * @param fragment
     * @param contact
     * @return true: 使用用户自定义点击事件；false：使用默认点击事件
     */
    @Override
    public boolean onListItemClick(Fragment fragment, IYWContact contact) {
        if (contact.getAppKey().equals(YWConstants.YWSDKAppKey)) {
            //TB或千牛客的服账号
            EServiceContact eServiceContact = new EServiceContact(contact.getUserId(), 0);//
            eServiceContact.setNeedByPass(false);
            Intent intent = LoginSampleHelper.getInstance().getIMKit().getChattingActivityIntent(eServiceContact);
            if(intent!=null)
                fragment.getActivity().startActivity(intent);
        } else {
            Intent intent =LoginSampleHelper.getInstance().getIMKit().getChattingActivityIntent(contact.getUserId(), contact.getAppKey());
            if(intent!=null)
                fragment.getActivity().startActivity(intent);
        }
        return true;
    }

    /**
     * 定制长按事件
     *
     * @param fragment
     * @param contact
     * @return true: 使用用户自定义长按事件；false：使用默认长按事件
     */
    @Override
    public boolean onListItemLongClick(Fragment fragment, final IYWContact contact) {
        final FragmentActivity mContext = fragment.getActivity();
        final IYWContactService contactService = LoginSampleHelper.getInstance().getIMKit().getContactService();
        final String[] items = new String[3];
        boolean isBlocked = contactService.isBlackContact(contact.getUserId(), contact.getAppKey());
        if (isBlocked) {
            items[0] = "移除黑名单";
        } else {
            items[0] = "加入黑名单";
        }
        items[1] = "删除好友";
        items[2] = "修改备注";
        if(!YWContactManager.isBlackListEnable()) {
            YWContactManager.enableBlackList();
        }
        //此处为示例代码
        new YWAlertDialog.Builder(mContext)
                .setTitle(contact.getUserId())
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (items[which].equals("加入黑名单")) {
                            contactService.addBlackContact(contact.getUserId(), contact.getAppKey(), new IWxCallback() {
                                @Override
                                public void onSuccess(Object... result) {
                                    IYWContact iywContact = (IYWContact) result[0];
                                    YWLog.i(TAG, "加入黑名单成功, id = " + iywContact.getUserId() + ", appkey = " + iywContact.getAppKey());
                                    ToastHelper.showToastMsg(mContext, "加入黑名单成功, id = " + iywContact.getUserId() + ", appkey = " + iywContact.getAppKey());
                                }

                                @Override
                                public void onError(int code, String info) {
                                    YWLog.i(TAG, "加入黑名单失败，code = " + code + ", info = " + info);
                                    ToastHelper.showToastMsg(mContext, "加入黑名单失败，code = " + code + ", info = " + info);
                                }

                                @Override
                                public void onProgress(int progress) {

                                }
                            });
                        } else if (items[which].equals("移除黑名单")) {
                            contactService.removeBlackContact(contact.getUserId(), contact.getAppKey(), new IWxCallback() {
                                @Override
                                public void onSuccess(Object... result) {
                                    IYWContact iywContact = (IYWContact) result[0];
                                    YWLog.i(TAG, "移除黑名单成功,  id = " + iywContact.getUserId() + ", appkey = " + iywContact.getAppKey());
                                    ToastHelper.showToastMsg(mContext, "移除黑名单成功,  id = " + iywContact.getUserId() + ", appkey = " + iywContact.getAppKey());
                                }

                                @Override
                                public void onError(int code, String info) {
                                    YWLog.i(TAG, "移除黑名单失败，code = " + code + ", info = " + info);
                                    ToastHelper.showToastMsg(mContext, "移除黑名单失败，code = " + code + ", info = " + info);
                                }

                                @Override
                                public void onProgress(int progress) {

                                }
                            });
                        }else if (items[which].equals("删除好友")) {
                            contactService.delContact(contact.getUserId(), contact.getAppKey(), new IWxCallback() {
                                @Override
                                public void onSuccess(Object... result) {
                                    IYWContact iywContact = (IYWContact) result[0];
                                    YWLog.i(TAG, "删除好友成功,  id = " + iywContact.getUserId() + ", appkey = " + iywContact.getAppKey());
                                    ToastHelper.showToastMsg(mContext, "删除好友成功,  id = " + iywContact.getUserId() + ", appkey = " + iywContact.getAppKey());
                                }

                                @Override
                                public void onError(int code, String info) {
                                    YWLog.i(TAG, "删除好友失败，code = " + code + ", info = " + info);
                                    ToastHelper.showToastMsg(mContext, "删除好友失败，code = " + code + ", info = " + info);
                                }

                                @Override
                                public void onProgress(int progress) {

                                }
                            });
                        }else if (items[which].equals("修改备注")) {
                            final String lRandomName = RandomNameUtil.getRandomName();
                            contactService.chgContactRemark(contact.getUserId(), contact.getAppKey(),lRandomName, new IWxCallback() {
                                @Override
                                public void onSuccess(Object... result) {
                                    IYWContact iywContact = (IYWContact) contact;
                                    YWLog.i(TAG, "修改备注成功,  id = " + iywContact.getUserId() + ", appkey = " + iywContact.getAppKey());
                                    ToastHelper.showToastMsg(mContext, "修改备注成功,  id = " + iywContact.getUserId() + " , appkey = " + iywContact.getAppKey()+" , 备注名 ＝ "+lRandomName);
                                }

                                @Override
                                public void onError(int code, String info) {
                                    YWLog.i(TAG, "修改备注失败，code = " + code + ", info = " + info);
                                    ToastHelper.showToastMsg(mContext, "修改备注失败，code = " + code + ", info = " + info);
                                }

                                @Override
                                public void onProgress(int progress) {

                                }
                            });
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create().show();
        return true;
    }
}
